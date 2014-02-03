angular.module('mainApp')
    .controller('VehicleController', ['$scope', 'VehicleService',
        function ($scope, VehicleService) {

            $scope.getIconUrl = function (vehicle) {
                if (!vehicle) {
                    return '';
                }
                //noinspection JSUnresolvedVariable
                return 'images/map_icons/' + vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
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

            var id = $scope.$stateParams.id;
            if (id) {
                VehicleService.vehicleById(id, function (successResponse) {
                        // TODO: Check status code
                        $scope.vehicle = successResponse.data.vehicle;

                        angular.forEach($scope.vehicle.vehicleImages, function (vehicleImage) {
                            $scope.addSlide(vehicleImage);
                        });
                    },
                    function (errorResponse) {
                        // TODO: Err0r Me$$age
                    });
            }
        }]);