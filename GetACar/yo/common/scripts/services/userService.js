angular.module('gacCommon')
    .factory('UserService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Finds all users.
                 *
                 * @return //TODO:
                 */
                allUsers: function (successFunction, errorFunction) {
                    return Restangular.all('users').getList()
                        .then(successFunction, errorFunction);
                },

                /**
                 * Finds a user by his id.
                 *
                 * @return //TODO:
                 */
                userById: function (id, successFunction, errorFunction) {
                    return Restangular.one('users', id).get()
                        .then(successFunction, errorFunction);
                },

                /**
                 * Changes the user.
                 */
                changeUser: function (changeUserFormData, successFunction, errorFunction) {
                    return Restangular.all('users').changeUser(changeUserFormData)
                        .then(successFunction, errorFunction);
                }
            };
        }]);