angular.module('mainApp')
    .controller('VehiclePositionController', ['$scope',
        function ($scope) {
            // Nothing right now!
            $scope.jumpToSearchResult = function () {
                var elm = document.getElementById('vsr' + $scope.parameter.result.vehicle.id);
                if (elm) {
                    elm.scrollIntoView();
                    // TODO: Because of the fixed header some results are not scrolled into view correctly / are hidden under the header then :-/ Could be complicated to fix though...
                    // TODO: Timer to highlight the result for some seconds!
                } else {
                    // TODO: What if we introduce pagination and land here?
                }
            };
        }]);