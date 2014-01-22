angular.module('adminApp')
    .directive('userListEntry', [
        function () {
            return {
                restrict: 'AE',
                scope: {
                    user: '=',
                    onSelect: '&',
                    selectedUser: '='
                },
                templateUrl: 'partials/userListEntry.html',
                controller: ['$scope', function ($scope) {

                }]
            };
        }]);