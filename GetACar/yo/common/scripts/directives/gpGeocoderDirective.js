/**
 * TODO: FIX DOC!!
 *
 * A directive which shows the textual version / address of coordinates with the help of Google Places.
 * Google places info: https://developers.google.com/maps/documentation/javascript/places
 *
 * Usage:
 *
 */
angular.module('gacCommon')
    .directive('gpGeocoder', [
        function () {
            return {
                restrict: 'E',
                template: '<span class="gp-geocoder">{{ geoCoded }}</span>',
                scope: {
                    gpGeocodeLatLng: '='
                },
                controller: ['$scope', function ($scope) {
                    // gpGeocoder watches this variable and tries to find the address for the coordinates
                    $scope.geoCoded = 'Looking up address...';
                }],
                link: function (scope, element, attrs, model) {
                    // watch latLng provided to directive
                    scope.watchGPGeocodeLatLng = function () {
                        return scope.gpGeocodeLatLng;
                    };

                    scope.$watch(scope.watchGPGeocodeLatLng, function () {
                        // TODO: How to check more than one level deep without such a long if?
                        if (scope.gpGeocodeLatLng && scope.gpGeocodeLatLng.lat && scope.gpGeocodeLatLng.lng) {
                            var geocoder = new google.maps.Geocoder();
                            var latlng = new google.maps.LatLng(scope.gpGeocodeLatLng.lat, scope.gpGeocodeLatLng.lng);
                            geocoder.geocode({'latLng': latlng}, function (results, status) {
                                if (status === google.maps.GeocoderStatus.OK) {
                                    if (results[1]) {
                                        scope.$apply(function () {
                                            /*jshint camelcase: false */
                                            scope.geoCoded = results[1].formatted_address;
                                        });
                                    } else {
                                        scope.$apply(function () {
                                            scope.geoCoded = 'Unknown Address';
                                        });
                                    }
                                } else {
                                    scope.$apply(function () {
                                        scope.geoCoded = 'Error: Could not fetch the address';
                                    });
                                }
                            });
                        }
                    }, true);
                }
            };
        }]);