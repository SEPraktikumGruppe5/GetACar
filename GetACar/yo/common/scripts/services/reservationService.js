angular.module('gacCommon')
    .factory('ReservationService', ['Restangular',
        function (Restangular) {
            return {

                reservationsByUser: function (successFunction, errorFunction) { // TODO: User as param!
                    return Restangular.one('reservations', 'byUser').getList()
                        .then(successFunction, errorFunction);
                },

                reservationById: function (id, successFunction, errorFunction) {
                    return Restangular.one('reservations', id).get()
                        .then(successFunction, errorFunction);
                },

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