angular.module('mainApp')
    .controller('ReservationsController', ['$scope', 'ReservationService',
        function ($scope, ReservationService) {

            $scope.getIconUrl = function (vehicle) {
                //noinspection JSUnresolvedVariable
                return 'images/map_icons/' + vehicle.vehicleType.icon; // TODO: Bind and inject icon path in rootScope
            };

            $scope.convertToDate = function (dateTimeInMs) { // TODO: Extract into date util service / factory
                if (!dateTimeInMs) {
                    return undefined;
                }
                return new Date(dateTimeInMs);
            };

            $scope.reservations = [];
            ReservationService.reservationsByUser(function (successResponse) {
                if (successResponse.data.reservations) {
                    $scope.reservations = [];
                    $scope.reservationEndPositions = [];
                    var count = 0;
                    angular.forEach(successResponse.data.reservations, function (reservation) {
                        $scope.reservations[count] = reservation;
                        var currentReservation = $scope.reservations[count];
                        currentReservation.count = count;

                        //noinspection JSUnresolvedVariable
                        $scope.reservationEndPositions[count] = {
                            lat: reservation.endLatitude,
                            lng: reservation.endLongitude
                        };
                        count = count + 1;
                    });
                }
            }, function (errorResponse) {
                // TODO: Error msg!
            });
        }]);