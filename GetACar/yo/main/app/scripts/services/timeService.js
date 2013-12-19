angular.module('mainApp')
    .factory('TimeService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Gets the current time from the server.
                 *
                 * @param successFunction
                 * @param errorFunction
                 * @returns {*}
                 */
                whatTimeIsIt: function (successFunction, errorFunction) {
                    return Restangular.all('time').whatTimeIsIt()
                        .then(successFunction, errorFunction);
                }
            };
        }]);