angular.module('accountApp')
    .controller('RegisterController', function ($scope, AccountService, $state) {
        $scope.user = {
            firstName: undefined
        };
        $scope.errors = {};

        $scope.register = function (user) {
            $scope.errors = {}; // clean up server errors

            AccountService.register(user,
                function (successResponse) {
                    var status = successResponse.status;
                    if (status === 201) {
                        $state.go('login', {justRegistered: true});
                    } else {
                        // TODO: Error message?
                    }
                },
                function (errorResponse) {
                    var status, data;
                    status = errorResponse.status;
                    data = errorResponse.data;
                    // 422 means validation errors
                    if (status === 422) {
                        $scope.registerForm.$setPristine();
                        angular.forEach(data.errors, function (errors, field) {
                            // tell the form that field is invalid
                            var registerFormField = $scope.registerForm[field];
                            if (registerFormField) {
                                registerFormField.$setValidity('server', false);
                                // keep the error messages from the server
                                $scope.errors[field] = errors.join(', ');
                            }
                        });
                    }
                });
        };
    });