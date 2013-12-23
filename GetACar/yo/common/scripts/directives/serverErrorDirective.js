angular.module('gacCommon')
    .directive('serverError', [
        function () {
            return {
//                restrict: 'A',
                require: 'ngModel',
                link: function (scope, element, attrs, modelController) {
//                    // set "valid" by default on typing
                    element.bind('keydown', function () {
                        scope.$apply(function () {
                            modelController.$setValidity('server', true);
                        });
                    });
                    element.bind('blur', function () {
                        scope.$apply(function () {
                            modelController.$setValidity('server', true);
                        });
                    });
                }
            };
        }]);