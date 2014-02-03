angular.module('mainApp')
    .controller('VehiclePositionController', ['$scope',
        function ($scope) {
            // Nothing right now!
            $scope.jumpToSearchResult = function () {
                var elm = document.getElementById('vsr' + $scope.parameter.result.vehicle.id);
                if (elm) {
                    elm.scrollIntoView();
                    window.scrollTo(window.scrollX, window.scrollY  - 60); // TODO: Get header height (60px) per jquery?
                    // TODO: Timer to highlight the result for some seconds!
                } else {
                    // TODO: What if we introduce pagination and land here?
                    // --> Never land here by only showing those that are on the current page on the map!
                }
            };
        }]);