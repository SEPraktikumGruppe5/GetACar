angular.module('accountApp')
    .controller('RegisterCtrl', function ($scope, $http) {
        $scope.user = {
            firstName: undefined
        };

        $scope.register = function (user) { // TODO: UserService with login / register?
            $scope.errors = {}; // clean up server errors
            $http({
                method: 'POST',
                url: '/getacar/rest/users/registerUser',
                data: user
            })
                .success(function (data, status, headers, config) {
                    // The back-end returns "202 - Accepted" on a successful login with the redirect URL in the
                    // 'Location' header parameter
//                    if (status === 202) {
//                        var location = headers('Location');
//                        $window.location.replace(location);
//                    }
                }).
                error(function (data, status, headers, config) {
                    // 422 means validation errors
                    if (status === 422) {
                        $scope.registerForm.$setPristine();
//                        $scope.registerForm.$setValidity(true);
                        angular.forEach(data.errors, function (errors, field) {
                            // tell the form that field is invalid
                            var registerFormField = $scope.registerForm[field];
                            if (registerFormField) {
                                registerFormField.$setValidity('server', false);
//                                registerFormField.$setPristine();
                                // keep the error messages from the server
                                $scope.errors[field] = errors.join(', ');
                            }
                        });
                    }
                });
        };
    });