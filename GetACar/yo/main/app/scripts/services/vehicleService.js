angular.module('mainApp')
    .factory('VehicleService', ['$http', 'Restangular',
        function ($http, Restangular) {
            return {
                /**
                 * Attempts to log the user in.
                 *
                 * @param {object} user The user object
                 * @param {string} user.loginName The login name for the login attempt
                 * @param {string} user.password The password for the login attempt
                 * @param {function} successFunction The function that is called when the call succeeds
                 * @param {function} errorFunction The function that is called when the call fails // TODO: Doc for functions?
                 * @return //TODO:
                 */
                login: function (user, successFunction, errorFunction) {
                    return Restangular.all('users').loginUser(user)
                        .then(successFunction, errorFunction);
                },
                /**
                 * Attempts to register the user.
                 *
                 * @param user The user object
                 * @param {string} user.loginName The login name of the user
                 * @param {string} user.password The password for the login attempt
                 * TODO: Document other options!
                 * @param {function} successFunction The function that is called when the call succeeds // TODO: Doc for functions?
                 * @param {function} errorFunction The function that is called when the call fails
                 * @return //TODO:
                 */
                register: function (user, successFunction, errorFunction) {
                    return Restangular.all('users').registerUser(user)
                        .then(successFunction, errorFunction);
                }
            };
        }]);