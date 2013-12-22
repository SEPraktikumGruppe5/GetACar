angular.module('mainApp')
    .directive('vehicleSearchResult', [
        function () {
            return {
                restrict: 'E',
                scope: {
                    result: '=result'
                },
                templateUrl: 'partials/vehicleSearchResult.html',
                controller: ['$scope', function ($scope) {

                }]
            };
        }]);