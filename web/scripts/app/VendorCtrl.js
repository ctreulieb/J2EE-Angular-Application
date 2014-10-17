// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: VendorCtrl.js
// Purpose: Controller for the Vendor page.
(function(app) {
var VendorCtrl = function($scope, $modal, RESTFactory, $filter) {
    var baseurl = 'webresources/vendor';
    //Init
    //Initializes variables and gets vendors from database
    var init = function() {
        $scope.status = 'Loading Vendors...';
        RESTFactory.restCall('get', baseurl, -1, '').then(function(vendors) {
            
            if (vendors.length > 0) { 
                $scope.vendors = vendors;
                $scope.status = 'Vendors Retrieved';
                $scope.vendor = $scope.vendors[0];
            }
            else {
                $scope.status = 'Vendors not retrieved code = ' + vendors;
            }
        }, function(reason) {
            $scope.status = 'Vendors not retrieved' + reason;
        });
        
    };
    init();
    //selectRow
    //Sets todo and prepares variables to add or modify a vendor
    //Then it opens the modal window to add or update/delete as appropriate
    $scope.selectRow = function(row, vendor) {
        if(row < 0 ) {
            $scope.todo = 'add';
            $scope.vendor = new Object();
        }
        else {
            $scope.vendor = vendor;
            $scope.selectedRow = row;
            $scope.todo = 'update';
        }
        var modalInstance = $modal.open({
            templateUrl: 'partials/vendorModal.html',
            controller: 'VendorModalCtrl',
            scope: $scope,
            backdrop: 'static'

        });

        modalInstance.result.then(function(results){
            if(results.operation === 'add')
            {
                if(results.vendorno > 0){
                    $scope.status = 'Vendor ' + results.vendorno + ' Added!';
                    $scope.selectedRow = $scope.vendors.length - 1;
                }                    
                else
                    $scope.status = 'Vendor not added!';
            }else if(results.operation === 'update')
            {
                if (results.numOfRows === 1) {
                    $scope.status = 'Vendor ' + results.vendorno + ' Updated!';
                }
                else {
                    $scope.status = 'Vendor Not Updated!';
                }
            }else if(results.operation === 'delete')
            {
                for (var i = 0; i <  $scope.vendors.length; i++) {
                    if ($scope.vendors[i].vendorno === results.vendorno) {
                        $scope.vendors.splice(i, 1);
                        break;
                    }
                }
                if(results.numOfRows === 1 ) {
                    $scope.selectedRow = null;
                    $scope.status = 'Vendor ' + results.vendorno + ' Deleted!';
                }
                else {
                    $scope.status = 'Vendor ' + results.vendorno + 'Not Deleted!';
                }
            }
        }, function() {
            if($scope.todo === 'update')
                $scope.status = 'Vendor Not Updated!';
            else if($scope.todo === 'add')
                $scope.status = 'Vendor not added!';
            else
                $scope.status = 'Other problem!';
        }, function(reason){
            $scope.status = reason;
        });
    };
};
app.controller('VendorCtrl', ['$scope', '$modal', 'RESTFactory', VendorCtrl]);
})(angular.module('case1'));  

