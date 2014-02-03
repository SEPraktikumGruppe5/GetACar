angular.module('accountApp', [
        'gacCommon'
    ])
    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
        $locationProvider.html5Mode(false).hashPrefix('');

        /* Routes */
        $urlRouterProvider.otherwise(function ($rootScope, $location) {
//            RedirectService.setUnresolvedPath($location.path());
//            $rootScope.unresolvedLocation = {
//                path: $location.path()
//            };
//            console.log($rootScope.unresolvedLocation);
            var unresolvedPath = $location.path();
            $location.path('login');
            if (unresolvedPath && unresolvedPath !== '') {
                $location.search('redirect', unresolvedPath);
            }
        });
        // Now set up the states
        $stateProvider
            .state('main', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'MainController'
            })
            .state('login', {
                url: 'login?justRegistered&redirect',
                parent: 'main',
                templateUrl: 'partials/login.html',
                controller: 'LoginController'
            })
            .state('register', {
                url: 'register',
                parent: 'main',
                templateUrl: 'partials/register.html',
                controller: 'RegisterController'
            });
    });