angular.module('accountApp')
    .controller('LoginController', ['$scope', 'AccountService', '$window', '$stateParams',
        function ($scope, AccountService, $window, $stateParams) {

            $scope.loginFormData = {
                timeSimulation : new Date()
            };
            $scope.errorMessage = undefined;
            $scope.errors = {};

            $scope.login = function (loginFormData) {
                // reset the error, the validations and the just registered message
                $scope.errorMessage = undefined;
                $scope.errors = {};
                $stateParams.justRegistered = undefined;

                AccountService.login(loginFormData,
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
                        // 422 means validation errors
                        if (status === 422) {
                            $scope.loginForm.$setPristine();
                            angular.forEach(data.errors, function (errors, field) {
                                // tell the form that field is invalid
                                var formField = $scope.loginForm[field];
                                if (formField) {
                                    formField.$setValidity('server', false);
                                    // keep the error messages from the server
                                    $scope.errors[field] = errors.join(', ');
                                }
                            });
                        }
                        if (status === 500) {
                            if (data.error) {
                                $scope.errorMessage = data.error;
                            } else {
                                $scope.errorMessage = 'Unknown Err0r!';
                            }
                        }
                    });
            };
        }]);