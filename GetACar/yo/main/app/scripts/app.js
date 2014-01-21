angular.module('mainApp', [
        'gacCommon',
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
            })
            .state('reservations', {
                url: 'reservations',
                parent: 'main',
                templateUrl: 'partials/reservations.html',
                controller: 'ReservationsController'
            })
            .state('reservation', {
                url: 'reservations/:id', // url: 'login?justRegistered', TODO: Parameters but not like that but mandatory
                parent: 'main',
                templateUrl: 'partials/reservation.html',
                controller: 'ReservationController'
            });
    });