angular.module('mainApp', [
        'gacCommon',
        'ngSanitize',
        'google-maps'
    ])
    .config(function ($stateProvider, $urlRouterProvider) {
        /* Routes */
        // For any unmatched url, redirect to /login
        $urlRouterProvider.otherwise('/home');
        // Now set up the states
        $stateProvider
            .state('main', {
                url: '/',
                templateUrl: 'views/main.html',
                controller: 'MainController'
            })
            .state('home', {
                url: 'home',
                parent: 'main',
                templateUrl: 'partials/home.html',
                controller: 'HomeController'
            })
            .state('search', {
                url: 'search',
                parent: 'main',
                templateUrl: 'partials/searchVehicles.html',
                controller: 'SearchVehiclesController'
            });
    });