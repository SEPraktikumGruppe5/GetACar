(function () {
    angular.module('adminApp', [
            'gacCommon',
            'google-maps',
            'blueimp.fileupload'
        ])
        .config(['$stateProvider', '$urlRouterProvider', 'fileUploadProvider',
            function ($stateProvider, $urlRouterProvider, fileUploadProvider) {
                /* Routes */
                // For any unmatched url, redirect to /userManagement
                $urlRouterProvider.otherwise('/userManagement');
                // Now set up the states
                $stateProvider
                    .state('main', {
                        url: '/',
                        templateUrl: 'views/main.html',
                        controller: 'MainController'
                    })
                    .state('userManagement', {
                        url: 'userManagement',
                        parent: 'main',
                        templateUrl: 'partials/userManagement.html',
                        controller: 'UserManagementController'
                    })
                    .state('vehicleManagement', {
                        url: 'vehicleManagement',
                        parent: 'main',
                        templateUrl: 'partials/vehicleManagement.html',
                        controller: 'VehicleManagementController'
                    });

                fileUploadProvider.defaults.handleResponse = function (e, data) {
                    var files = data.result;
                    if (files) {
                        data.scope().replace(data.files, files);
                        // do what you want...
                    } else if (data.errorThrown || data.textStatus === 'error') {
                        data.files[0].error = data.errorThrown ||
                            data.textStatus;
                    }
                };
            }])
        .controller('FileDestroyController', [
            '$scope', '$http',
            function ($scope, $http) {
                var file = $scope.file,
                    state;
                if (file.url) {
                    file.$state = function () {
                        return state;
                    };
                    file.$destroy = function () {
                        state = 'pending';
                        return $http({
                            url: file.deleteUrl,
                            method: file.deleteType
                        }).then(
                            function () {
                                state = 'resolved';
                                $scope.clear(file);
                            },
                            function () {
                                state = 'rejected';
                            }
                        );
                    };
                } else if (!file.$cancel && !file._index) {
                    file.$cancel = function () {
                        $scope.clear(file);
                    };
                }
            }
        ]);
}());