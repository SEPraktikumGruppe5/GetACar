angular.module('mainApp')
    .directive('vehicleSearchResult', [
        function () {
            return {
                restrict: 'E',
                scope: {
                    result: '=result'
                },
                templateUrl: 'partials/vehicleSearchResult.html',
                controller: ['$scope', 'ModalService', function ($scope, ModalService) {

                    $scope.getIconUrl = function (result) {
                        return 'images/map_icons/' + result.vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
                    };

                    // gpGeocoder watches this variable and tries to find the address for the coordinates
                    $scope.gpGeocodeLatLng = {
                        lat: $scope.result.currentLatitude,
                        lng: $scope.result.currentLongitude
                    };

                    $scope.rentVehicle = function () {
                        var modalDefaults, modalOptions;
                        modalDefaults = {
                            backdrop: true,
                            keyboard: true,
                            modalFade: true,
                            templateUrl: 'partials/rentVehicleModal.html'
                        };

                        modalOptions = {
                            result: $scope.result
                        };

                        ModalService.showModal(modalDefaults, modalOptions).then(
                            function (result) {
                                window.alert('kK!');
                            });
                    };
                }]
            };
        }]);