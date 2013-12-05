angular.module('accountApp').factory('AccountService', ['$http', function ($http) { // TODO: Use Restangular here instead of $http!
    return {
        /**
         * Attempts to register the user.
         *
         * @param user The user object
         * @param {string} user.loginName The login name of the user
         * @param {string} user.password The password for the login attempt
         */
        register: function (user) {
            return $http({
                method: 'POST',
                url: '/getacar/rest/users/registerUser',
                data: user
            });
        },
        /**
         * Attempts to log the user in.
         *
         * @param {object} user The user object
         * @param {string} user.loginName The login name for the login attempt
         * @param {string} user.password The password for the login attempt
         */
        login: function (user) {
            return $http({
                method: 'POST',
                url: '/getacar/rest/users/loginUser',
                data: user
            });
        }
    };
}]);