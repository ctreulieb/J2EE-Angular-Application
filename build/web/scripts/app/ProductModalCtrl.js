// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: ProductModalCtrl.js
// Purpose: Controller for the Product modal window.
(function(app) {
var ProductModalCtrl = function($scope, $modalInstance, RESTFactory) {
    var baseurl = 'webresources/product';
    var retVal = {operation: '', productcode: -1, numOfRows: -1};
    
    //Init 
    //Initializes variables and gets vendors from database
    var init = function() {
        RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors) {
            
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
    
    //Update
    //Updates a product and closes the modal window
    $scope.update = function() {
        RESTFactory.restCall('put', baseurl, -1, $scope.product).then(function(results){
           retVal.operation = 'update';
           retVal.productcode = $scope.product.productcode;
           
           if(results.substring) {
               retVal.numOfRows =  parseInt(results);
           }
           else {
               retVal.numOfRows = -1;
               retVal = results;
           }
           
           $modalInstance.close(retVal);
        }, function(reason) {
            retVal = 'Product was not updated! - system error ' + reason;
            $modalInstance.close(retVal);
        });
        
    };//update
    
    //Add
    //Add's a product and closes the modal window
    $scope.add = function() {
        $scope.status = "Wait...";
        for(var p = 0;  p < $scope.products.length; p++)
        {
            if($scope.products[p].productcode === $scope.product.productcode) {
                retVal.productcode = $scope.product.productcode;
                retVal.numOfRows = -9;
                retVal.operation = 'add';
                $modalInstance.close(retVal);
                return;
            }
        }
        RESTFactory.restCall('post', baseurl, -1, $scope.product).then(function(results) {
            if(results.substring) {
                if (parseInt(results) > 0){
                    $scope.products.push($scope.product);
                    retVal.operation = 'add';
                    retVal.numOfRows = 1;
                    retVal.productcode = $scope.product.productcode;
                    $modalInstance.close(retVal);
                }
                else {
                    retVal = 'Product not added! - System error ' + results;
                    $modalInstance.close(parseInt(results));
                }
            }
            else {
                retVal = 'Product not added! - System error ' + results;
                $modalInstance.close(parseInt(results));
            }
              
            
            
        }, function(error) {
            retVal = 'Product not added! - Error: ' + error;
            $modalInstance.close(retVal);
        });
    }; // add
    
    //Cancel
    //Closes the modal window
    $scope.cancel = function() {
        $scope.status = "";
        $modalInstance.close(retVal);
    }; //cancel
    
    //Del
    //Deletes selected product and closes the modal window
    $scope.del = function() {
        RESTFactory.restCall('delete', baseurl, $scope.product.productcode, '').then(function(results) {
            retVal.operation = 'delete';
            retVal.productcode = $scope.product.productcode;
            
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

app.controller('ProductModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', ProductModalCtrl]);
})(angular.module('case1'));  