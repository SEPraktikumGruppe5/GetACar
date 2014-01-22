angular.module('adminApp')
    .directive('vehicleListEntry', [
        function () {
            return {
                restrict: 'AE',
                scope: {
                    vehicle: '=',
                    onSelect: '&',
                    selectedVehicle: '='
                },
                templateUrl: 'partials/vehicleListEntry.html',
                controller: ['$scope', function ($scope) {

                }]
            };
        }]);