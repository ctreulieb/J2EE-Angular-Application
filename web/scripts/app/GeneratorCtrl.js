// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: GeneratorCtrl.js
// Purpose: Controller for the Purchase order generator page.
(function (app) {
    var GeneratorCtrl = function($scope, RESTFactory, $window) {
        var baseurl = 'webresources/generator';
        //Init
        //Initializes variables and gets the vendors from the database
        var init = function() {
        RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors) {
            
            if (vendors.length > 0) { 
                $scope.vendors = vendors;
                $scope.status = 'Vendors Retrieved';
                $scope.vendor = $scope.vendors[0];
                $scope.notcreated = true;
                $scope.subtotal = -1;
            }
            else {
                $scope.status = 'Vendors not retrieved code = ' + vendors;
            }
        }, function(reason) {
            $scope.status = 'Vendors not retrieved' + reason;
        });
        
    };
    init();
    
    //SelectVendor
    //Selects a vendor and retrieves that vendors products from DB
    $scope.selectVendor = function() {
        $scope.pickedVendor = true;
        $scope.subtotal = -1;
        $scope.tax = 0;
        $scope.total = 0;
        $scope.notcreated = true;
        $scope.pono = null;
        var v;
        $scope.vendors.forEach(function(v)
        {
            if ($scope.purchaseOrder.vendorno === v.vendorno)
                $scope.vendor = v;
        });
        RESTFactory.restCall('get', 'webresources/product', $scope.purchaseOrder.vendorno, '').then(function(products) {
            if(products.length > 0) {
                $scope.products = products;
                $scope.status = "Products for " + $scope.vendor.name + " loaded!";
                $scope.product = $scope.products[0];
                for(var pCount = 0; pCount < $scope.products.length; pCount++)
                    $scope.products[pCount].qty = 0;
            }
            else {
                $scope.products = null;
                $scope.status = 'Products not retrieved code = ' + products;
            }
        }, function(reason) {
            $scope.status = 'Products not retrieved' + reason;
        });
    };//select vendor
    
    //addToPo
    //Adds a product and quantity to the purchase order, extended price and taxes etc are calculated as well
    $scope.addToPO =  function() {
        $scope.subtotal = 0;
        $scope.tax = 0;
        $scope.total = 0;
        for(var pCount = 0; pCount < $scope.products.length; pCount++)
        {
           if($scope.purchaseOrder.productcode === $scope.products[pCount].productcode)
           {
               if($scope.purchaseOrder.qty === 'EOQ')
                   $scope.products[pCount].qty = $scope.products[pCount].eoq;
               else
                   $scope.products[pCount].qty = $scope.purchaseOrder.qty;
               $scope.products[pCount].extended = $scope.products[pCount].qty * $scope.products[pCount].costprice;
           }
           
           $scope.subtotal += ($scope.products[pCount].qty * $scope.products[pCount].costprice);

        };
        
        $scope.tax = $scope.subtotal * 0.13;
        $scope.total = $scope.subtotal + $scope.tax;
        
        
        
    }; //add to po
    
    //viewPDF
    //Links to the PDF servlet to view the Purchase order invoice for the just completed purchase order
    $scope.viewPdf = function() {
        $window.location.href = 'POPDF?po=' + $scope.pono;
    }; // viewPdf
    
    //CreatePO
    //Adds the current Purchase order to the database
    $scope.createPO = function() {
        $scope.status = "Wait...";
        var PODTO = new Object();
        PODTO.total = $scope.total;
        PODTO.vendorno = $scope.vendor.vendorno;
        PODTO.items = $scope.products;
        
        $scope.PO = RESTFactory.restCall('post', 'webresources/po',
                                $scope.vendor.vendorno,
                                PODTO).then(function(results){
            if (results.length > 0) {
                $scope.status = 'PO ' + results + ' created!';
                $scope.pono = results;
                $scope.notcreated = false;
                $scope.generatorForm.$setPristine();
            }
            else {
                $scope.status = 'PO not created - ' + results;
            }
        }, function(reason){
            $scope.status = 'PO not created - ' + reason;
        });
    };//create po
};
app.controller('GeneratorCtrl', ['$scope', 'RESTFactory', '$window', GeneratorCtrl]);
})(angular.module('case1'));