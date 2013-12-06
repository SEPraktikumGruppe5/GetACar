angular.module('accountApp')
    .controller('LoginController', function ($scope, AccountService, $window, $stateParams) {

        $scope.user = {
            loginName: undefined
        };
        $scope.errorMessage = undefined;

        $scope.login = function (user) {
            // reset the error and the just registered message
            $scope.errorMessage = undefined;
            $stateParams.justRegistered = undefined;

            AccountService.login(user,
                function (successResponse) {
                    var status, headers, location;
                    status = successResponse.status;
                    headers = successResponse.headers;
                    // The back-end returns "202 - Accepted" on a successful login with the redirect URL in the
                    // 'Location' header parameter
                    if (status === 202) {
                        location = headers('Location');
                        if (location) {
                            $window.location.replace(location);
                        } else {
                            $scope.errorMessage = 'Unknown Err0r!'; // TODO: ErrorMessageDirective?
                        }
                    }
                },
                function (errorResponse) {
                    var status, data;
                    status = errorResponse.status;
                    data = errorResponse.data;
                    if (status === 500) {
                        if (data.error) {
                            $scope.errorMessage = data.error;
                        } else {
                            $scope.errorMessage = 'Unknown Err0r!';
                        }
                    }
                });
        };
    });