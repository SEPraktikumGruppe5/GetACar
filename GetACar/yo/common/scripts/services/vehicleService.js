angular.module('gacCommon')
    .factory('VehicleService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Finds all vehicles.
                 *
                 * @return //TODO:
                 */
                allVehicles: function (successFunction, errorFunction) {
                    return Restangular.all('vehicles').getList()
                        .then(successFunction, errorFunction);
                },

                /**
                 * Finds a vehicle by its id.
                 *
                 * @return //TODO:
                 */
                vehicleById: function (id, successFunction, errorFunction) {
                    return Restangular.one('vehicles', id).get()
                        .then(successFunction, errorFunction);
                },

                /**
                 * Creates the vehicle.
                 */
                createVehicle: function (createVehicleFormData, successFunction, errorFunction) {
                    return Restangular.all('vehicles').createVehicle(createVehicleFormData)
                        .then(successFunction, errorFunction);
                },

                /**
                 * Changes the vehicle.
                 */
                changeVehicle: function (changeVehicleFormData, successFunction, errorFunction) {
                    return Restangular.all('vehicles').changeVehicle(changeVehicleFormData)
                        .then(successFunction, errorFunction);
                },

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