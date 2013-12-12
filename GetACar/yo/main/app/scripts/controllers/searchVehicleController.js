angular.module('mainApp')
    .controller('SearchVehicleController', ['$scope', 'GeoService',
        function ($scope, GeoService) {
            $scope.cities = function (namePart) {
                return GeoService.cities(namePart, 10, function (successResponse) {
                    var cities;
                    cities = [];
                    for (var i in successResponse.data.cities) {
                        cities.push({name: successResponse.data.cities[i].name, index: i});
                    }
                    return cities;
                }, function (errorResponse) {

                });
            };

        }]);