angular.module('mainApp')
    .controller('ReservationController', ['$scope', 'ReservationService',
        function ($scope, ReservationService) {

            $scope.getIconUrl = function (reservation) {
                if (!reservation) {
                    return '';
                }
                //noinspection JSUnresolvedVariable
                return 'images/map_icons/' + reservation.vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
            };

            $scope.convertToDate = function (dateTimeInMs) { // TODO: Extract into date util service / factory
                if (!dateTimeInMs) {
                    return undefined;
                }
                return new Date(dateTimeInMs);
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

            // Small workaround to prevent the 'grey box' google maps problem
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
                        latitude: undefined,
                        longitude: undefined
                    },
                    options: {
                        streetViewControl: false,
                        panControl: false
                    },
                    zoom: 12,
                    dragging: false,
                    bounds: {},
                    startPositionMarker: {
                        latitude: undefined, // TODO: Initialize with start coordinates
                        longitude: undefined,
                        icon: undefined,
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

            var id = $scope.$stateParams.id;
            if (id) {
                ReservationService.reservationById(id, function (successResponse) {
                        // TODO: Check status code
                        $scope.reservation = successResponse.data.reservation;

                        $scope.reservationEndPosition = {
                            lat: $scope.reservation.endLatitude,
                            lng: $scope.reservation.endLongitude
                        };

                        $scope.map.center.latitude = $scope.reservation.endLatitude; // TODO: Use start coordinate here?
                        $scope.map.center.longitude = $scope.reservation.endLongitude;

//                        $scope.map.startPositionMarker.latitude = $scope.reservation.startLatitude; // TODO: Once available
//                        $scope.map.startPositionMarker.longitude = $scope.reservation.startLongitude;
                        $scope.map.startPositionMarker.icon = 'images/map_icons/' +
                            $scope.reservation.vehicle.vehicleType.icon; // TODO: Inject path

                        $scope.map.endPositionMarker.latitude = $scope.reservation.endLatitude;
                        $scope.map.endPositionMarker.longitude = $scope.reservation.endLongitude;

                        angular.forEach($scope.reservation.vehicle.vehicleImages, function (vehicleImage) {
                            $scope.addSlide(vehicleImage);
                        });
                    },
                    function (errorResponse) {
                        // TODO: Err0r Me$$age
                    });
            }
        }]);