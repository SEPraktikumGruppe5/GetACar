angular.module('adminApp')
    .directive('userListEntry', [
        function () {
            return {
                restrict: 'E',
                scope: {
                    user: '=',
                    onSelect: '&'
                },
                templateUrl: 'partials/userListEntry.html',
                controller: ['$scope', function ($scope) {

                }]
            };
        }]);