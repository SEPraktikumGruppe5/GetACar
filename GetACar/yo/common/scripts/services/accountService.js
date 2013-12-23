angular.module('gacCommon')
    .factory('AccountService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Attempts to log the user in.
                 *
                 * @param {object} loginFormData The user object
                 * @param {string} loginFormData.loginName The login name for the login attempt
                 * @param {string} loginFormData.password The password for the login attempt
                 * @param {function} successFunction The function that is called when the call succeeds
                 * @param {function} errorFunction The function that is called when the call fails // TODO: Doc for functions?
                 * @return //TODO:
                 */
                login: function (loginFormData, successFunction, errorFunction) {
                    return Restangular.all('users').loginUser(loginFormData)
                        .then(successFunction, errorFunction);
                },
                /**
                 * Attempts to register the user.
                 *
                 * @param registerFormData The user object
                 * @param {string} registerFormData.loginName The login name of the user
                 * @param {string} registerFormData.password The password for the login attempt
                 * TODO: Document other options!
                 * @param {function} successFunction The function that is called when the call succeeds // TODO: Doc for functions?
                 * @param {function} errorFunction The function that is called when the call fails
                 * @return //TODO:
                 */
                register: function (registerFormData, successFunction, errorFunction) {
                    return Restangular.all('users').registerUser(registerFormData)
                        .then(successFunction, errorFunction);
                }
            };
        }]);