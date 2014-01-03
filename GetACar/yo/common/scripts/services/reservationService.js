angular.module('gacCommon')
    .factory('ReservationService', ['Restangular',
        function (Restangular) {
            return {
                /**
                 * Reserves a vehicle. TODO: Doc Ready
                 *
                 * @param reserveVehicleFormData
                 * @param successFunction
                 * @param errorFunction
                 * @returns {*}
                 */
                reserveVehicle: function (reserveVehicleFormData, successFunction, errorFunction) {
                    return Restangular.all('reservations').reserveVehicle(reserveVehicleFormData)
                        .then(successFunction, errorFunction);
                }
            };
        }]);