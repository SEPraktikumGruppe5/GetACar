/**
 * TODO: FIX DOC!!
 *
 * A directive for adding google places autocomplete to a text box
 * google places autocomplete info: https://developers.google.com/maps/documentation/javascript/places
 *
 * Usage:
 *
 * <input type="text" gp-autocomplete="result" details="details" options="options"/>
 *
 * + `gp-autocomplete="result"`: specifies the directive, $scope.result will hold the text box autocomplete result
 *
 * + `details="details"`: $scope.details will hold the autocomplete's more detailed result; latLng. address components, etc.
 *
 * + `options="options"`: options provided by the user that filter the autocomplete results
 *
 * - options = {
 *      types: type, string, values can be 'geocode', 'establishment', '(regions)', or '(cities)'
 *      bounds: bounds, google maps LatLngBounds Object
 *      country: country string, ISO 3166-1 Alpha-2 compatible country code. examples; 'ca', 'us', 'gb'
 *   }
 */
angular.module('gacCommon')
    .directive('gpAutocomplete', [
        function () {
            return {
                restrict: 'A',
                scope: {
                    gpOptions: '=',
                    gpValue: '=',
                    gpDetails: '=',
                    gpLatLng: '=',
                    gpPlaceChanged: '&onPlaceChanged'
                },
                link: function (scope, element, attrs, model) {
                    // options for autocomplete
                    var opts;

                    // convert options provided to opts
                    var initOpts = function () {
                        opts = {};
                        if (scope.options) {
                            if (scope.options.types) {
                                opts.types = [];
                                opts.types.push(scope.options.types);
                            }
                            if (scope.options.bounds) {
                                opts.bounds = scope.options.bounds;
                            }
                            if (scope.options.country) {
                                opts.componentRestrictions = {
                                    country: scope.options.country
                                };
                            }
                        }
                    };
                    initOpts();

                    // create new autocomplete
                    // reinitializes on every change of the options provided
                    var newAutocomplete = function () {
                        scope.gPlace = new google.maps.places.Autocomplete(element[0], opts);
                        google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
                            scope.gpValue = element.val();
                            scope.gpDetails = scope.gPlace.getPlace();
                            scope.gpPlaceChanged({value: scope.gpValue, details: scope.gpDetails});
                            scope.$apply();
                        });
                    };
                    newAutocomplete();

                    // watch options provided to directive
                    scope.watchOptions = function () {
                        return scope.options;
                    };

                    scope.$watch(scope.watchOptions, function () {
                        initOpts();
                        newAutocomplete();
                        element[0].value = '';
                        scope.gpPlaceChanged({value: undefined, details: undefined});
                    }, true);

                    // watch value provided to directive
                    scope.watchGPValue = function () {
                        return scope.gpValue;
                    };

                    scope.$watch(scope.watchGPValue, function () {
                        element[0].value = '';
                        if (scope.gpValue) {
                            element[0].value = scope.gpValue;
                        }
                    });

                    // watch latLng provided to directive
                    scope.watchGPLatLng = function () {
                        return scope.gpLatLng;
                    };

                    scope.$watch(scope.watchGPLatLng, function () {
                        if (scope.gpLatLng && scope.gpLatLng.lat && scope.gpLatLng.lng) { // TODO: How to check more than one level deep without such a long if?
                            var geocoder = new google.maps.Geocoder();
                            var latlng = new google.maps.LatLng(scope.gpLatLng.lat, scope.gpLatLng.lng);
                            geocoder.geocode({'latLng': latlng}, function (results, status) {
                                if (status === google.maps.GeocoderStatus.OK) {
                                    if (results[1]) {
                                        /*jshint camelcase: false */
                                        element[0].value = results[1].formatted_address;
                                    } else {
                                        // TODO No results found message
                                    }
                                } else { // TODO: Retry on too many requests like in gpGeocoderDirective
                                    // TODO Error message
                                }
                            });
                        }
                    }, true);
                }
            };
        }]);