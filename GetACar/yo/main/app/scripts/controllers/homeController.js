angular.module('mainApp')
    .controller('HomeController', ['$scope',
        function ($scope) {
            var homeSlides = $scope.homeSlides = [];
            $scope.addSlide = function (image) {
                homeSlides.push({
                    image: 'images/home_slides/' + image
                });
            };
            $scope.addSlide('burningman2.jpg');
            $scope.addSlide('burningman.jpg');
            $scope.addSlide('crazyfrog.jpg');
        }]);