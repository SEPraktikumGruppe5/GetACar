angular.module('mainApp', [
        'ngCookies',
        'ngSanitize',
        'ui.router',
        'restangular',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker',
        'google-maps',
        'gpAutocomplete'
    ])
    .config(function ($stateProvider, $urlRouterProvider, RestangularProvider) {
        /* Routes */ // TODO: Try to configure this in a separate .js file!
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

        /* Restangular */ // TODO: Try to configure this in a separate .js file!
        RestangularProvider.setBaseUrl('/getacar/rest/'); // TODO: Change baseUrl to sth. more standard for Rest stuff -> /getacar/api/v1/ ?
        // Include headers and everything else in every response
        RestangularProvider.setFullResponse(true);
        // Define custom Restangular methods
        RestangularProvider.addElementTransformer('vehicles', true, function (vehicles) {
            // This will add a method called searchVehicles that will do a POST to the path searchVehicles
            // signature is (name, operation, path, params, headers, elementToPost)
            vehicles.addRestangularMethod('searchVehicles', 'post', 'searchVehicles');
            return vehicles;
        });
        RestangularProvider.addElementTransformer('time', true, function (time) {
            time.addRestangularMethod('whatTimeIsIt', 'get', 'whatTimeIsIt');
            return time;
        });
    })
    .run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            // It's very handy to add references to $state and $stateParams to the $rootScope
            // so that you can access them from any scope within your applications.For example,
            // <li ng-class="{ active: $state.includes('contacts.list') }"> will set the <li>
            // to active whenever 'contacts.list' or one of its descendants is active.
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }]);