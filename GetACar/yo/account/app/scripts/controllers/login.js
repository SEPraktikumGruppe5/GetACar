angular.module('accountApp').controller('LoginCtrl', function ($scope, $http, $window) {
    $scope.user = {
        loginName: undefined
    };
    $scope.login = function (user) {
        $http({ // TODO: UserService with login / register?
            method: 'POST',
            url: '/getacar/rest/users/loginUser',
            data: user
        })
            .success(function (data, status, headers, config) {
                // The back-end returns "202 - Accepted" on a successful login with the redirect URL in the
                // 'Location' header parameter
                if (status === 202) {
                    var location = headers('Location');
                    $window.location.replace(location);
                }
            }).
            error(function (data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
    };
});