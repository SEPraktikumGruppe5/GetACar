angular.module('mainApp')
    .controller('VehiclePositionController', ['$scope',
        function ($scope) {
            // Nothing right now!
            $scope.jumpToSearchResult = function () {
                var elm = document.getElementById('vsr' + $scope.parameter.result.vehicle.id);
                if (elm) {
                    elm.scrollIntoView();
                    // TODO: Timer to highlight the result for some seconds!
                } else {
                    // TODO: What if we introduce pagination and land here?
                }
            };
        }]);