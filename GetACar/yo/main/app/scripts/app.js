angular.module('mainApp', [
        'ngCookies',
        'ngSanitize',
        'ui.router',
        'restangular',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker'
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
                templateUrl: 'partials/search.html',
                controller: 'SearchVehicleController'
            });

        /* Restangular */ // TODO: Try to configure this in a separate .js file!
        RestangularProvider.setBaseUrl('/getacar/rest/'); // TODO: Change baseUrl to sth. more standard for Rest stuff -> /getacar/api/v1/ ?
        // Include headers and everything else in every response
        RestangularProvider.setFullResponse(true);
        // Define cities method
        RestangularProvider.addElementTransformer('geo', true, function (geo) {
            // This will add a method called cities that will do a GET to the path cities
            // signature is (name, operation, path, params, headers, elementToPost)
            geo.addRestangularMethod('cities', 'get', 'cities');

            return geo;
        });
//        // Define registerUser method
//        RestangularProvider.addElementTransformer('users', true, function (user) {
//            // This will add a method called loginUser that will do a POST to the path loginUser
//            // signature is (name, operation, path, params, headers, elementToPost)
//            user.addRestangularMethod('registerUser', 'post', 'registerUser');
//
//            return user;
//        });
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