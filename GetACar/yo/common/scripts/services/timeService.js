angular.module('gacCommon')
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
                    return Restangular.all('times').simulated()
                        .then(successFunction, errorFunction);
                }
            };
        }]);