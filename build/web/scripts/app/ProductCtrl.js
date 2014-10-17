// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: ProductCtrl.js
// Purpose: Controller for the Product page.
(function (app) {
    var ProductCtrl = function($scope, $modal, RESTFactory, $filters) {
        var baseurl = 'webresources/product';
        
        //Init
        //Initializes variables and gets products from the database
        var init = function() {
            $scope.status = 'Loading Products...';
            RESTFactory.restCall('get', baseurl, -1, '').then(function(products) {
                if(products.length > 0) {
                    $scope.products = products;
                    $scope.status = 'Products Retrieved';
                    $scope.product = $scope.products[0];
                }
                else {
                    $scope.status = 'Products not retrieved code = ' + products;
                }
            }, function(reason) {
                $scope.status = 'Products not retrieved' + reason;
            });
        };
        init();
        //selectRow
        //Sets todo and prepares variables to add or modify a product
        //Then it opens the modal window to add or update/delete as appropriate
        $scope.selectRow = function(row, product) {
        if(row < 0 ) {
            $scope.todo = 'add';
            $scope.product = new Object();
        }
        else {
            $scope.product = product;
            $scope.selectedRow = row;
            $scope.todo = 'update';
        }
        var modalInstance = $modal.open({
            templateUrl: 'partials/productModal.html',
            controller: 'ProductModalCtrl',
            scope: $scope,
            backdrop: 'static'

        });
        
        modalInstance.result.then(function(results){
            if(results.operation === 'add')
            {
                if(results.numOfRows > 0){
                    $scope.status = 'Product ' + results.productcode + ' Added!';
                    $scope.selectedRow = $scope.products.length - 1;
                }                    
                else
                    $scope.status = 'Product not added!';
            }else if(results.operation === 'update')
            {
                if (results.numOfRows === 1) {
                    $scope.status = 'Product ' + results.productcode + ' Updated!';
                }
                else {
                    $scope.status = 'Product ' + results.productcode + ' Not Updated!';
                }
            }else if(results.operation === 'delete')
            {
                for (var i = 0; i <  $scope.products.length; i++) {
                    if ($scope.products[i].productcode === results.productcode) {
                        $scope.products.splice(i, 1);
                        break;
                    }
                }
                if(results.numOfRows === 1 ) {
                    $scope.selectedRow = null;
                    $scope.status = 'Product ' + results.productcode + ' Deleted!';
                }
                else {
                    $scope.status = 'Product ' + results.productcode + 'Not Deleted!';
                }
            }
        }, function() {
            if($scope.todo === 'update')
                $scope.status = 'Product Not Updated!';
            else if($scope.todo === 'add')
                $scope.status = 'Product not added!';
            else
                $scope.status = 'Other problem!';
        }, function(reason){
            $scope.status = reason;
        });
    };
};
app.controller('ProductCtrl', ['$scope', '$modal', 'RESTFactory', ProductCtrl]);
})(angular.module('case1'));
