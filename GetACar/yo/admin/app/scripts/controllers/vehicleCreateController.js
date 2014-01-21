angular.module('adminApp')
    .controller('VehicleCreateController', ['$scope', 'VehicleService', 'VehicleTypeService', '$filter',
        function ($scope, VehicleService, VehicleTypeService, $filter) {
            $scope.createVehicleFormData = {
                vehicle: {
                    vehicleImages: []
                }
            };
            $scope.vehicleTypes = [];
            VehicleTypeService.allVehicleTypes(function (successResponse) {
                var vehicleTypes;
                vehicleTypes = [];
                if (successResponse.data.vehicleTypes) {
                    vehicleTypes = successResponse.data.vehicleTypes;
                }
                $scope.vehicleTypes = vehicleTypes;
            }, function (errorResponse) {
                // TODO: Error message!
            });
            $scope.activeOptions = [
                {value: true, name: 'True'},
                {value: false, name: 'False'}
            ];

            $scope.errors = {}; // empty initialization of server errors

            $scope.vehicleImages = [];
            $scope.addSlide = function (vehicleImage) {
//                var newWidth = 600 + vehicleImages.length;
                $scope.vehicleImages.push({
                    image: '../images?image=' + vehicleImage.fileName,
                    fileName: vehicleImage.fileName
//                    text: ['More', 'Extra', 'Lots of', 'Surplus'][vehicleImages.length % 4] + ' ' +
//                        ['Cats', 'Kittys', 'Felines', 'Cutes'][vehicleImages.length % 4]
                });
            };

            function remove(arr, item) {
                var i;
                for (i = arr.length; i >= 0; i = i - 1) {
                    if (arr[i] === item) {
                        arr.splice(i, 1);
                    }
                }
            }

            $scope.removeImage = function (vehicleImage) {
                if (vehicleImage && vehicleImage.fileName) {
                    angular.forEach($scope.vehicleImages, function (vi) {
                        if (vehicleImage.fileName === vi.fileName) {
                            remove($scope.vehicleImages, vi);
                        }
                    });
                    angular.forEach($scope.createVehicleFormData.vehicle.vehicleImages, function (vi) {
                        if (vehicleImage.fileName === vi.fileName) {
                            remove($scope.createVehicleFormData.vehicle.vehicleImages, vi);
                        }
                    });
                }
            };

            // Image uploads
            // Options you want to pass to jQuery File Upload e.g.:
            $scope.uploadOptions = {
                url: '../uploads',
                maxFileSize: 5000000,
                acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
            };

            // Listen to fileuploadstop event
            $scope.$on('fileuploadstop', function (e, data) {
                console.log('All uploads have finished');
                var files = $scope.queue;
                angular.forEach(files, function (file) {
                    $scope.createVehicleFormData.vehicle.vehicleImages.push({
                        fileName: file.name
                    });
                });
                $scope.vehicleImages = [];
                angular.forEach($scope.createVehicleFormData.vehicle.vehicleImages, function (vehicleImage) {
                    $scope.addSlide(vehicleImage);
                });
            });

            $scope.formVisible = false;
            $scope.formTabSelected = function (selected) {
                $scope.formVisible = selected;
                if ($scope.formVisible) {
                    $scope.queue = [];
                }
            };

            // bindings of the gp-autocomplete-box
            $scope.gpOptions = undefined;
            $scope.gpValue = undefined;
            $scope.gpDetails = undefined;
            $scope.gpLatLng = undefined;

            // callback of gp-autocomplete box
            $scope.onGPPlaceChanged = function (val, details) {
                if (details) {
                    $scope.createVehicleFormData.vehicle.initialLatitude = $filter('number')(details.geometry.location.lat(), 7);
                    $scope.createVehicleFormData.vehicle.initialLongitude = $filter('number')(details.geometry.location.lng(), 7);
                }
            };

//            function initialize(vehicleToSelect) { // TODO: Introduce initialize()-function in other controllers, too!
//                VehicleService.allVehicles(function (successFunction) {
//                    if (successFunction.data.vehicles) {
//                        // TODO: Check status code
//                        $scope.vehicles = successFunction.data.vehicles;
//                        if (!vehicleToSelect) {
//                            // there must be at least one vehicle as we are here!
//                            $scope.onVehicleSelect($scope.vehicles[0]);
//                        } else {
//                            var found = false;
//                            angular.forEach($scope.vehicles, function (vehicle) {
//                                if (vehicle.id === vehicleToSelect.id && !found) {
//                                    $scope.onVehicleSelect(vehicle);
//                                    found = true;
//                                }
//                            });
//                            if (!found) {
//                                $scope.onVehicleSelect($scope.vehicles[0]);
//                            }
//                        }
//                    }
//                }, function (errorFunction) {
//                    // TODO: Error msg
//                });
//            }
//
//            // init
//            initialize(undefined);

            $scope.save = function (createVehicleFormData) {
                var createVehicleFormDataToSend = angular.copy(createVehicleFormData);

                VehicleService.createVehicle(createVehicleFormDataToSend, function (successResponse) {
                        var status = successResponse.status;
                        // Created
                        if (status === 201) {
                            // reinitialize
                            $scope.modalOptions.ok(); // TODO: Pass the created vehicle back to automatically select it
                        } else {
                            // TODO: Message? What could have gone wrong?
                        }
                    },
                    function (errorResponse) {
                        var status, data;
                        status = errorResponse.status;
                        data = errorResponse.data;
                        // 422 means validation errors
                        if (status === 422) {
                            $scope.createVehicleForm.$setPristine();
                            angular.forEach(data.errors, function (errors, field) {
                                // tell the form that field is invalid
                                var formField = $scope.createVehicleForm[field];
                                if (formField) {
                                    formField.$setValidity('server', false);
                                    // keep the error messages from the server
                                    $scope.errors[field] = errors.join('; ');
                                }
                            });
                        }
                    }
                );
            };

            $scope.resetChanges = function () {
                $scope.createVehicleFormData = {
                    vehicle: {
                        vehicleImages: []
                    }
                };
                // reset image uploads
                $scope.queue = [];

                // reset invalid fields
                angular.forEach($scope.createVehicleForm, function (formField) {
                    if (formField.$setValidity) {
                        // tell the form that field is valid
                        formField.$setValidity('server', true);
                    }
                });
            };
        }]);