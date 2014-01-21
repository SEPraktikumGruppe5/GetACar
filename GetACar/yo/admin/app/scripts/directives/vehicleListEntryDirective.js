angular.module('adminApp')
    .directive('vehicleListEntry', [
        function () {
            return {
                restrict: 'E',
                scope: {
                    vehicle: '=',
                    onSelect: '&'
                },
                templateUrl: 'partials/vehicleListEntry.html',
                controller: ['$scope', function ($scope) {

                }]
            };
        }]);