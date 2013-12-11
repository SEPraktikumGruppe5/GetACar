angular.module('accountApp')
    .controller('RegisterController', ['$scope', 'AccountService', '$state', 'ModalService',
        function ($scope, AccountService, $state, ModalService) {
            // At least initialize the form-data at "object level" for deserialization reasons at server-side
            $scope.registerFormData = {
                user: {}
            };
            $scope.errors = {}; // empty initialization of server errors

            $scope.readTOS = function () {
                var modalOptions = {
                    closeButtonText: 'Cancel',
                    actionButtonText: 'Ok',
                    headerText: 'TOS',
                    bodyText: 'TOS of Get A Car!'
                };

                ModalService.showModal({}, modalOptions).then(function (result) {
                    $scope.registerFormData.acceptTOS = true;
                });
            };

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
                                var formField = $scope.registerForm[field];
                                if (formField) {
                                    formField.$setValidity('server', false);
                                    // keep the error messages from the server
                                    $scope.errors[field] = errors.join(', ');
                                }
                            });
                        }
                    });
            };
        }]);