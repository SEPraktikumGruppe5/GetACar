angular.module('adminApp')
    .controller('UserManagementController', ['$scope', 'UserService',
        function ($scope, UserService) {
            $scope.users = [];
            $scope.selectedUser = undefined;
            $scope.changeUserFormData = {
                user: undefined
            };
            $scope.activeOptions = [
                {value: true, name: 'True'},
                {value: false, name: 'False'}
            ];

            $scope.errors = {}; // empty initialization of server errors

            function initialize(userToSelect) { // TODO: Introduce initialize()-function in other controllers, too!
                UserService.allUsers(function (successFunction) {
                    if (successFunction.data.users) {
                        // TODO: Check status code
                        $scope.users = successFunction.data.users;
                        if (!userToSelect) {
                            // there must be at least one user as we are here!
                            $scope.onUserSelect($scope.users[0]);
                        } else {
                            var found = false;
                            angular.forEach($scope.users, function (user) {
                                if (user.id === userToSelect.id && !found) {
                                    $scope.onUserSelect(user);
                                    found = true;
                                }
                            });
                            if (!found) {
                                $scope.onUserSelect($scope.users[0]);
                            }
                        }
                    }
                }, function (errorFunction) {
                    // TODO: Error msg
                });
            }

            // init
            initialize(undefined);

            // callback function of userListEntryDirective
            $scope.onUserSelect = function (user) {
                if (user) {
                    $scope.selectedUser = user;
                    $scope.changeUserFormData.user = angular.copy(user);
                } else {
                    $scope.selectedUser = undefined;
                    $scope.changeUserFormData.user = undefined;
                }
            };

            $scope.saveChanges = function (changeUserFormData) {
                var changeUserFormDataToSend = angular.copy(changeUserFormData);

                UserService.changeUser(changeUserFormDataToSend, function (successResponse) {
                        var status = successResponse.status;
                        // OK
                        if (status === 200) {
                            // reinitialize
                            initialize(changeUserFormDataToSend.user);
                        } else {
                            // TODO: Message? What could have gone wrong?
                        }
                    },
                    function (errorResponse) {
                        var status, data;
                        status = errorResponse.status;
                        data = errorResponse.data;
                        // 422 means validation errors
                        if (status === 422) {
                            $scope.changeUserForm.$setPristine();
                            angular.forEach(data.errors, function (errors, field) {
                                // tell the form that field is invalid
                                var formField = $scope.changeUserForm[field];
                                if (formField) {
                                    formField.$setValidity('server', false);
                                    // keep the error messages from the server
                                    $scope.errors[field] = errors.join('; ');
                                }
                            });
                        }
                    }
                );
            };

            $scope.resetChanges = function () {
                $scope.onUserSelect($scope.selectedUser);
            };
        }]);