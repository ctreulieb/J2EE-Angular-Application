// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: VendorModalCtrl.js
// Purpose: Controller for the Vendor Modal window.
(function(app) {
var VendorModalCtrl = function($scope, $modalInstance, RESTFactory) {
    var baseurl = 'webresources/vendor';
    var retVal = {operation: '', vendorno: -1, numOfRows: -1};
    
    //Update
    //Updates selected vendor
    $scope.update = function() {
        RESTFactory.restCall('put', baseurl, -1, $scope.vendor).then(function(results){
           retVal.operation = 'update';
           retVal.vendorno = $scope.vendor.vendorno;
           
           if(results.substring) {
               retVal.numOfRows =  parseInt(results);
           }
           else {
               retVal.numOfRows = -1;
               retVal = results;
           }
           
           $modalInstance.close(retVal);
        }, function(reason) {
            retVal = 'Vendor was not updated! - system error ' + reason;
            $modalInstance.close(retVal);
        });
        
    };//update
    //Add
    //Adds a new vendor
    $scope.add = function() {
        $scope.status = "Wait...";
        RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results) {
            if(results.substring) {
                if (parseInt(results) > 0){
                    $scope.vendor.vendorno = parseInt(results);
                    $scope.vendors.push($scope.vendor);
                    retVal.operation = 'add';
                    retVal.numOfRows = 1;
                    retVal.vendorno = parseInt(results);
                    $modalInstance.close(retVal);
                }
                else {
                    retVal = 'Vendor not added! - System error ' + results;
                    $modalInstance.close(parseInt(results));
                }
            }
            else {
                retVal = 'Vendor not added! - System error ' + results;
                $modalInstance.close(parseInt(results));
            }
              
            
            
        }, function(error) {
            retVal = 'Vendor not added! - Error: ' + error;
            $modalInstance.close(retVal);
        });
    }; // add
    
    //Cancel
    //Closes modal window
    $scope.cancel = function() {
        $scope.status = "";
        $modalInstance.close(retVal);
    }; //cancel
    //Del
    //Deletes selected vendor
    $scope.del = function() {
        RESTFactory.restCall('delete', baseurl, $scope.vendor.vendorno, '').then(function(results) {
            retVal.operation = 'delete';
            retVal.vendorno = $scope.vendor.vendorno;
            
            if(results.substring) {
                retVal.numOfRows = parseInt(results);
            }
            else {
                retVal.numOfRows = -1;
            }
            
            $modalInstance.close(retVal);
        }, function() {
            retVal.numOfRows = -1;
            $modalInstance.close(retVal);
        });
    }; //delete
};

app.controller('VendorModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', VendorModalCtrl]);
})(angular.module('case1'));  