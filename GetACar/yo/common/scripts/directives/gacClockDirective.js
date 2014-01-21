angular.module('gacCommon')
    .directive('gacClock', [
        function () {
            return {
                restrict: 'E',
                scope: {},
                template: '<div class="clock">{{ time | date:\'medium\' }}</div>',
                controller: ['$scope', '$window', 'TimeService', function ($scope, $window, TimeService) {
                    TimeService.whatTimeIsIt(function (successResponse) {
                        var time = successResponse.data.time;
                        if (time) {
                            $scope.time = new Date(time);
                            $window.setInterval(function someWork() {
                                $scope.$apply(function () {
                                    $scope.time = new Date(+$scope.time + 1000);
                                });
                            }, 1000);
                        }
                    }, function (errorResponse) {
                        // TODO: Error?
                    });
                }]//,
//                link: function (scope, element, attributes, controller) {
//                    scope.$watch('time', function(newVal) {
//                        // the `$watch` function will fire even if the
//                        // weather property is undefined, so we'll
//                        // check for it
//                        if (newVal) {
//
//                        }
//                    });
//                }
            };
        }]);