angular.module('accountApp', [
        'gacCommon'
    ])
    .config(function ($stateProvider, $urlRouterProvider) {
        /* Routes */
        // For any unmatched url, redirect to /login
        $urlRouterProvider.otherwise('/login');
        // Now set up the states
        $stateProvider
            .state('main', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'MainController'
            })
            .state('login', {
                url: 'login?justRegistered',
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