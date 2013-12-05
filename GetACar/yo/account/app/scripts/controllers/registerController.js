angular.module('accountApp')
    .controller('RegisterController', function ($scope, AccountService, $state) {
        $scope.user = {
            firstName: undefined
        };

        $scope.register = function (user) { // TODO: UserService with login / register?
            $scope.errors = {}; // clean up server errors
            AccountService.register(user)
                .success(function (data, status, headers, config) {
                    // TODO: Check status code!
                    $state.go('login', {justRegistered: true});
                })
                .error(function (data, status, headers, config) {
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