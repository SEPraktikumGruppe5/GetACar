angular.module('accountApp', [
        'ngCookies',
        'ngSanitize',
        'ui.router'
    ])
    // TODO: Shows how to use a http interceptor -> Use in app to check for unauthorized / unauthenticated responses
//    .factory('getACarHttpInterceptor', function ($q, $injector) {
//        return {
//            'response': function (response) {
//                var state, status;
//                state = $injector.get('$state');
//                status = response.status;
//                if (status === 202) {
//                    state.go('register');
//                }
//                return response || $q.when(response);
//            }
//        };
//    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
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

//        $httpProvider.interceptors.push('getACarHttpInterceptor');
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