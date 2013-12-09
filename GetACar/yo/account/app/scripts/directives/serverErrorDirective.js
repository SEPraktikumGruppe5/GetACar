angular.module('accountApp')
    .directive('serverError', function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attrs, ctrl) {
                // set "valid" by default on typing
                element.bind('change', function () {
                    scope.$apply(function () {
                        ctrl.$setValidity('server', true);
                    });
                });
                element.bind('blur', function () {
                    scope.$apply(function () {
                        ctrl.$setValidity('server', true);
                    });
                });
            }
        };
    });