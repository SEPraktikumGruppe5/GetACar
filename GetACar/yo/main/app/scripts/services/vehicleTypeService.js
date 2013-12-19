angular.module('mainApp')
    .factory('VehicleTypeService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Finds all vehicle types.
                 *
                 * @return //TODO:
                 */
                allVehicleTypes: function (successFunction, errorFunction) {
                    return Restangular.all('vehicleTypes').getList()
                        .then(successFunction, errorFunction);
                }
            };
        }]);