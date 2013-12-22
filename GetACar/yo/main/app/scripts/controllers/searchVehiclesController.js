angular.module('mainApp')
    .controller('SearchVehiclesController', ['$scope', 'VehicleTypeService', 'VehicleService', '$filter',
        function ($scope, VehicleTypeService, VehicleService, $filter) {

            $scope.searched = false;
            $scope.searchVehiclesFormData = {
                // init fields here?
            };
            $scope.errors = {}; // empty initialization of server errors
            $scope.vehicleSearchResults = []; // empty initialization of search results

            $scope.vehicleTypes = [];
            VehicleTypeService.allVehicleTypes(function (successResponse) {
                var vehicleTypes;
                vehicleTypes = [];
                if (successResponse.data.vehicleTypes) {
                    vehicleTypes = successResponse.data.vehicleTypes;
                }
                $scope.vehicleTypes = vehicleTypes;
            }, function (errorResponse) {
                // TODO: Error message!
            });

            // bindings of the gp-autocomplete-box
            $scope.gpOptions = undefined;
            $scope.gpValue = undefined;
            $scope.gpDetails = undefined;
            $scope.gpLatLng = undefined;

            $scope.geolocationAvailable = navigator.geolocation ? true : false;

            var pinAndCenter = function (latitude, longitude) {
                $scope.$apply(function () {
                    $scope.map.userPositionMarker.latitude = latitude;
                    $scope.map.userPositionMarker.longitude = longitude;
                    $scope.map.userPositionMarker.showWindow = false;
                    $scope.map.userPositionMarker.templateParameter = {
                        position: {
                            latitude: latitude,
                            longitude: longitude
                        }
                    };
                    $scope.map.center.latitude = latitude;
                    $scope.map.center.longitude = longitude;
                    $scope.map.zoom = 15;
                });
            };

            $scope.findMe = function () {
                if ($scope.geolocationAvailable) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        pinAndCenter(position.coords.latitude, position.coords.longitude);
                        $scope.gpLatLng = {
                            lat: position.coords.latitude,
                            lng: position.coords.longitude
                        };
                        $scope.$apply();
                    }, function () {
                        // TODO: Is this the error method? Show error?
                    });
                }
            };

            // Enable the new Google Maps visuals
            google.maps.visualRefresh = true;

            // callback of gp-autocomplete box
            $scope.onGPPlaceChanged = function (val, details) {
                $scope.map.userPositionMarker.latitude = undefined;
                $scope.map.userPositionMarker.longitude = undefined;
                if (details) {
                    pinAndCenter(details.geometry.location.lat(), details.geometry.location.lng());
                }
            };

            // extend the scope by the 'map' object
            angular.extend($scope, {
                map: {
                    showTraffic: false,
                    showBicycling: false,
                    showWeather: false,
                    center: {
                        latitude: 51.165691,
                        longitude: 10.451526
                    },
                    options: {
                        streetViewControl: false,
                        panControl: false
                    },
                    zoom: 6,
                    dragging: false,
                    bounds: {},
                    vehicleSearchResultsMarkers: [],
                    userPositionMarker: {
                        latitude: undefined,
                        longitude: undefined,
                        showWindow: false,
                        templateUrl: 'partials/userPosition.html',
                        templateParameter: {
                            position: {}
                        },
                        onClicked: function (marker) {
                            $scope.$apply(function () {
                                marker.showWindow = true;
                            });
                        },
                        closeClick: function (marker) {
                            $scope.$apply(function () {
                                marker.showWindow = false;
                            });
                        }
                    }
                }
            });

            $scope.removeMarkers = function () {
                $scope.map.vehicleSearchResultsMarkers.length = 0;
                $scope.map.userPositionMarker.latitude = undefined;
                $scope.map.userPositionMarker.longitude = undefined;
            };

            $scope.removeVehicleSearchResultsMarkers = function () {
                $scope.map.vehicleSearchResultsMarkers.length = 0;
            };

            $scope.$watch('vehicleSearchResults', function () {
                $scope.removeVehicleSearchResultsMarkers();
                $scope.map.center.latitude = $scope.map.userPositionMarker.latitude;
                $scope.map.center.longitude = $scope.map.userPositionMarker.longitude;
                $scope.map.zoom = 12; // TODO: Dependant of chosen radius!
                if ($scope.vehicleSearchResults) {
                    angular.forEach($scope.vehicleSearchResults,
                        function (vehicleSearchResult) {
                            $scope.map.vehicleSearchResultsMarkers.push(
                                {
                                    icon: 'images/map_icons/' + vehicleSearchResult.vehicle.vehicleType.icon, // TODO: Dependant of vehicleType (introduce icon field in vehicleType)
                                    latitude: vehicleSearchResult.currentLatitude,
                                    longitude: vehicleSearchResult.currentLongitude,
                                    showWindow: false,
                                    templateUrl: 'partials/vehiclePosition.html',
                                    templateParameter: {
                                        distance: vehicleSearchResult.distance
                                    },
                                    onClicked: function (marker) {
                                        $scope.$apply(function () {
                                            marker.showWindow = true;
                                        });
                                    },
                                    closeClick: function (marker) {
                                        $scope.$apply(function () {
                                            marker.showWindow = false;
                                        });
                                    }
                                }
                            );
                        });
                }
            });

            $scope.searchVehicles = function (searchVehiclesFormData) {
                var searchVehiclesFormDataToSend = angular.copy(searchVehiclesFormData);
                searchVehiclesFormDataToSend.position = {
                    longitude: $scope.map.userPositionMarker.longitude,
                    latitude: $scope.map.userPositionMarker.latitude
                };
                VehicleService.searchVehicles(searchVehiclesFormDataToSend, function (successResponse) {
                    $scope.searched = true;
                    var data = successResponse.data;
                    $scope.vehicleSearchResults = data.vehicleSearchResults;
                }, function (errorResponse) {
                    var status, data;
                    status = errorResponse.status;
                    data = errorResponse.data;
                    // 422 means validation errors
                    if (status === 422) {
                        $scope.searchVehiclesForm.$setPristine();
                        angular.forEach(data.errors, function (errors, field) {
                            // tell the form that field is invalid
                            var formField = $scope.searchVehiclesForm[field];
                            if (formField) {
                                formField.$setValidity('server', false);
                                // keep the error messages from the server
                                $scope.errors[field] = errors.join(', ');
                            }
                        });
                    }
                });
            };
        }]);