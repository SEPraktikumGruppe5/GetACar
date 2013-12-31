angular.module('mainApp')
    .controller('RentVehicleController', ['$scope',
        function ($scope) {
            // convert data for easier usage
            $scope.vehicle = $scope.modalOptions.result.vehicle;
            $scope.vehicle.currentLongitude = $scope.modalOptions.result.currentLongitude;
            $scope.vehicle.currentLatitude = $scope.modalOptions.result.currentLatitude;
            $scope.vehicle.distance = $scope.modalOptions.result.distance;
            $scope.searchParameters = $scope.modalOptions.result.searchParameters;

            $scope.getIconUrl = function (vehicle) {
                return 'images/map_icons/' + vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
            };

            $scope.gpGeocodeLatLng = {
                lat: $scope.vehicle.currentLatitude,
                lng: $scope.vehicle.currentLongitude
            };

            $scope.rentVehicleFormData = {
                from: new Date($scope.searchParameters.from),
                to: new Date($scope.searchParameters.to)
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

            // Enable the new Google Maps visuals
            google.maps.visualRefresh = true;

            // callback of gp-autocomplete box
            $scope.onGPEndPlaceChanged = function (val, details) {
//                $scope.map.userPositionMarker.latitude = undefined;
//                $scope.map.userPositionMarker.longitude = undefined;
//                if (details) {
//                    pinAndCenter(details.geometry.location.lat(), details.geometry.location.lng());
//                }
            };

            $scope.rentVehicle = function () {


                // return to the modal caller
                $scope.modalOptions.ok();
            };
        }]);