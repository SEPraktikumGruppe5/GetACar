angular.module('accountApp')
    .controller('LoginController', function ($scope, AccountService, $window) {

        $scope.user = {
            loginName: undefined
        };

        $scope.login = function (user) {
            AccountService.login(user)
                .success(function (data, status, headers, config) {
                    // The back-end returns "202 - Accepted" on a successful login with the redirect URL in the
                    // 'Location' header parameter
                    if (status === 202) {
                        var location = headers('Location');
                        if (location) {
                            $window.location.replace(location);
                        }
                        // TODO: Else? Show error somehow
                    }
                }).
                error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                });
        };
    });