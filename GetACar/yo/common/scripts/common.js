angular.module('gacCommon', [
        'ngCookies',
        'ngSanitize',
        'ui.bootstrap',
        'ui.bootstrap.datetimepicker',
        'ui.router',
        'restangular'
    ])
    .config(function (RestangularProvider) {

        /* Restangular */

        RestangularProvider.setBaseUrl('/getacar/rest/v1/');
        // Include headers and everything else in every response
        RestangularProvider.setFullResponse(true);

        // Define custom Restangular methods
        RestangularProvider.addElementTransformer('users', true, function (user) {
            // This will add a method called loginUser that will do a POST to the path loginUser
            // signature is (name, operation, path, params, headers, elementToPost)
            user.addRestangularMethod('loginUser', 'post', 'loginUser');
            return user;
        });
        RestangularProvider.addElementTransformer('users', true, function (user) {
            user.addRestangularMethod('registerUser', 'post', 'registerUser');
            return user;
        });
        RestangularProvider.addElementTransformer('vehicles', true, function (vehicles) {
            vehicles.addRestangularMethod('searchVehicles', 'post', 'searchVehicles');
            return vehicles;
        });
        RestangularProvider.addElementTransformer('vehicles', true, function (vehicles) {
            vehicles.addRestangularMethod('changeVehicle', 'post', 'change');
            return vehicles;
        });
        RestangularProvider.addElementTransformer('vehicles', true, function (vehicles) {
            vehicles.addRestangularMethod('createVehicle', 'post', 'create');
            return vehicles;
        });
        RestangularProvider.addElementTransformer('times', true, function (times) {
            times.addRestangularMethod('simulated', 'get', 'simulated');
            return times;
        });
        RestangularProvider.addElementTransformer('reservations', true, function (reservations) {
            reservations.addRestangularMethod('reserveVehicle', 'post', 'reserveVehicle');
            return reservations;
        });
        RestangularProvider.addElementTransformer('users', true, function (users) {
            users.addRestangularMethod('changeUser', 'post', 'change');
            return users;
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