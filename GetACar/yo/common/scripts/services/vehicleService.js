angular.module('gacCommon')
    .factory('VehicleService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Searches for vehicles. TODO: Doc Ready
                 *
                 * @param searchVehiclesFormData
                 * @param successFunction
                 * @param errorFunction
                 * @returns {*}
                 */
                searchVehicles: function (searchVehiclesFormData, successFunction, errorFunction) {
                    return Restangular.all('vehicles').searchVehicles(searchVehiclesFormData)
                        .then(successFunction, errorFunction);
                }
            };
        }]);