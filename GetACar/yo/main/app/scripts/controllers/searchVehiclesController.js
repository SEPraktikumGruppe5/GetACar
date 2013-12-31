angular.module('mainApp')
    .controller('SearchVehiclesController', ['$scope', 'VehicleTypeService', 'VehicleService', '$filter', '$window',
        function ($scope, VehicleTypeService, VehicleService, $filter, $window) {

            $scope.searched = false;
            $scope.searchVehiclesFormData = {
                // init fields here?
            };
            $scope.errors = {}; // empty initialization of server errors

//            $scope.vehicleSearchResults = []; // empty initialization of search results
//            $scope.lastSearchParameters = {}; // empty initialization of search last parameters

            $scope.vehicleSearchResults = [
                {'vehicle': {'id': 6, 'vehicleType': {'id': 1, 'name': 'Car', 'icon': 'car.png', 'description': 'A car'}, 'licenseNumber': 'C-AR 6', 'vehicleImages': [], 'initialLatitude': 50.8427310, 'initialLongitude': 12.9149690, 'active': true, 'comment': 'Sechstes Auto'}, 'currentLongitude': 12.9149690, 'currentLatitude': 50.8427310, 'distance': 0.956264600875621},
                {'vehicle': {'id': 1, 'vehicleType': {'id': 1, 'name': 'Car', 'icon': 'car.png', 'description': 'A car'}, 'licenseNumber': 'C-IA 666', 'vehicleImages': [], 'initialLatitude': 52.5200000, 'initialLongitude': 13.4000000, 'active': true, 'comment': 'Ein Fahrzeug'}, 'currentLongitude': 12.9298980, 'currentLatitude': 50.8127220, 'distance': 2.94982181363976},
                {'vehicle': {'id': 7, 'vehicleType': {'id': 1, 'name': 'Car', 'icon': 'car.png', 'description': 'A car'}, 'licenseNumber': 'S-IE 777', 'vehicleImages': [], 'initialLatitude': 50.8427310, 'initialLongitude': 12.9149700, 'active': true, 'comment': 'The Magic Number'}, 'currentLongitude': 12.9053440, 'currentLatitude': 50.8042220, 'distance': 4.18663417489601}
            ]; // TODO: Set on empty array again!
            $scope.lastSearchParameters = {
                'position': {
                    'longitude': 12.927389,
                    'latitude': 50.839203
                }, 'radius': 10,
                'vehicleType': {
                    'id': 1,
                    'name': 'Car',
                    'icon': 'car.png',
                    'description': 'A car'
                },
                'from': 1386029100000,
                'to': 1386119400000
            }; // TODO: Set on empty object again!

            $scope.addSearchParametersToResult = function (result) {
                if (result) {
                    result.searchParameters = $scope.lastSearchParameters; // TODO: Test if we should better copy?
                }
                return result;
            };

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
                                    icon: 'images/map_icons/' + vehicleSearchResult.vehicle.vehicleType.icon,
                                    latitude: vehicleSearchResult.currentLatitude,
                                    longitude: vehicleSearchResult.currentLongitude,
                                    showWindow: false,
                                    templateUrl: 'partials/vehiclePosition.html',
                                    templateParameter: {
                                        result: vehicleSearchResult
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
                    $scope.lastSearchParameters = data.searchParameters;
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