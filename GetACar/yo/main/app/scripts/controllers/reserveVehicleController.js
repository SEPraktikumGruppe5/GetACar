angular.module('mainApp')
    .controller('ReserveVehicleController', ['$scope', 'ReservationService',
        function ($scope, ReservationService) {
            // convert data for easier usage
            $scope.vehicle = $scope.modalOptions.result.vehicle;
            $scope.vehicle.currentLongitude = $scope.modalOptions.result.currentLongitude;
            $scope.vehicle.currentLatitude = $scope.modalOptions.result.currentLatitude;
            $scope.vehicle.distance = $scope.modalOptions.result.distance;
            $scope.searchParameters = $scope.modalOptions.result.searchParameters;

            $scope.errors = {}; // empty initialization of server errors

            $scope.getIconUrl = function (vehicle) {
                return 'images/map_icons/' + vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
            };

            var vehicleImages = $scope.vehicleImages = [];
            $scope.addSlide = function (vehicleImage) {
//                var newWidth = 600 + vehicleImages.length;
                vehicleImages.push({
                    image: '../images?image=' + vehicleImage.fileName
//                    text: ['More', 'Extra', 'Lots of', 'Surplus'][vehicleImages.length % 4] + ' ' +
//                        ['Cats', 'Kittys', 'Felines', 'Cutes'][vehicleImages.length % 4]
                });
            };
            angular.forEach($scope.vehicle.vehicleImages, function(vehicleImage) {
                $scope.addSlide(vehicleImage);
            });

            $scope.gpGeocodeLatLng = {
                lat: $scope.vehicle.currentLatitude,
                lng: $scope.vehicle.currentLongitude
            };

            function convertToDate(dateTimeInMs) { // TODO: Extract into date util service / factory
                if (!dateTimeInMs) {
                    return undefined;
                }
                return new Date(dateTimeInMs);
            }

            $scope.reserveVehicleFormData = {
                vehicle: {
                    id: $scope.vehicle.id
                },
                startPosition: {
                    longitude: $scope.vehicle.currentLongitude,
                    latitude: $scope.vehicle.currentLatitude
                },
                endPosition: {
                    longitude: undefined,
                    latitude: undefined
                },
                startTime: convertToDate($scope.searchParameters.startTime),
                endTime: convertToDate($scope.searchParameters.endTime)
            };

            // bindings of the gp-autocomplete-boxes
            $scope.gpStartOptions = undefined;
            $scope.gpStartValue = undefined;
            $scope.gpStartDetails = undefined;
            $scope.gpStartLatLng = {
                lat: $scope.vehicle.currentLatitude,
                lng: $scope.vehicle.currentLongitude
            };
            $scope.gpEndOptions = undefined;
            $scope.gpEndValue = undefined;
            $scope.gpEndDetails = undefined;
            $scope.gpEndLatLng = undefined;

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

            // Small workaround to prevent the "grey box" google maps problem
            $scope.mapVisible = false;
            $scope.otherTabSelected = function () {
                $scope.mapVisible = false;
            };
            $scope.mapTabSelected = function () {
                $scope.mapVisible = true;
            };

            // Enable the new Google Maps visuals
            google.maps.visualRefresh = true;

            // extend the scope by the 'map' object
            angular.extend($scope, {
                map: {
                    showTraffic: false,
                    showBicycling: false,
                    showWeather: false,
                    center: {
                        latitude: $scope.vehicle.currentLatitude,
                        longitude: $scope.vehicle.currentLongitude
                    },
                    options: {
                        streetViewControl: false,
                        panControl: false
                    },
                    zoom: 12,
                    dragging: false,
                    bounds: {},
                    vehiclePositionMarker: {
                        latitude: $scope.vehicle.currentLatitude,
                        longitude: $scope.vehicle.currentLongitude,
                        icon: 'images/map_icons/' + $scope.vehicle.vehicleType.icon, // TODO: Inject path
                        showWindow: false
//                        templateUrl: 'partials/vehiclePosition.html',
//                        templateParameter: {
//                            position: {}
//                        },
//                        onClicked: function (marker) {
//                            $scope.$apply(function () {
//                                marker.showWindow = true;
//                            });
//                        },
//                        closeClick: function (marker) {
//                            $scope.$apply(function () {
//                                marker.showWindow = false;
//                            });
//                        }
                    },
                    endPositionMarker: {
                        latitude: undefined,
                        longitude: undefined,
                        icon: 'images/map_icons/' + 'flag.png', // TODO: Inject path
                        showWindow: false
//                        templateUrl: 'partials/vehiclePosition.html',
//                        templateParameter: {
//                            position: {}
//                        },
//                        onClicked: function (marker) {
//                            $scope.$apply(function () {
//                                marker.showWindow = true;
//                            });
//                        },
//                        closeClick: function (marker) {
//                            $scope.$apply(function () {
//                                marker.showWindow = false;
//                            });
//                        }
                    }
                }
            });

            // callback of gp-autocomplete box
            $scope.onGPEndPlaceChanged = function (val, details) {
                if (details) {
                    $scope.map.endPositionMarker.latitude = details.geometry.location.lat();
                    $scope.map.endPositionMarker.longitude = details.geometry.location.lng();
                    // Maybe better read the value on reserve-button-click?
                    $scope.reserveVehicleFormData.endPosition.longitude = details.geometry.location.lng();
                    $scope.reserveVehicleFormData.endPosition.latitude = details.geometry.location.lat();
                }
            };

            $scope.reserveVehicle = function () {
                ReservationService.reserveVehicle($scope.reserveVehicleFormData, function (successResponse) {
                    // return to the modal caller
                    $scope.modalOptions.ok($scope.reserveVehicleFormData);
                }, function (errorResponse) {
                    var status, data;
                    status = errorResponse.status;
                    data = errorResponse.data;
                    // 422 means validation errors
                    if (status === 422) {
                        $scope.reserveVehicleForm.$setPristine();
                        angular.forEach(data.errors, function (errors, field) {
                            // tell the form that field is invalid
                            var formField = $scope.reserveVehicleForm[field];
                            if (formField) {
                                formField.$setValidity('server', false);
                                // keep the error messages from the server
                                $scope.errors[field] = errors.join('; ');
                            }
                        });
                    }
                });
            };
        }]);