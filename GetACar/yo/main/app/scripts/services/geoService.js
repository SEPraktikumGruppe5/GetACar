angular.module('mainApp')
    .factory('GeoService', ['$http', 'Restangular',
        function ($http, Restangular) {
            return {
                /**
                 * Searches for cities by name.
                 *
                 * @param {string} namePart The name part of the city to search for
                 * @param {int} maxResults The maximum number of results to be returned
                 * @param {function} successFunction The function that is called when the call succeeds
                 * @param {function} errorFunction The function that is called when the call fails
                 * @return //TODO:
                 */
                cities: function (namePart, maxResults, successFunction, errorFunction) {
                    return Restangular.all('geo').customGET("cities", {namePart: namePart, maxResults: maxResults})
                        .then(successFunction, errorFunction);
                }
            };
        }]);