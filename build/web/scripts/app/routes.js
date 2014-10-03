/* routes.js
 * Used to setup routes for partial pages and match the page with 
 * the correct controller
 */
(function(app) {
    app.config(['$routeProvider', function($routeProvider) {
            $routeProvider
                    .when('/', {
                        controller: '',
                        templateUrl: 'partials/home.html'
                    })
                    .when('/vendor', {
                        controller: 'VendorCtrl',
                        templateUrl: 'partials/vendor.html'
                    })
                    .when('/product', {
                        controller: 'ProductCtrl',
                        templateUrl: 'partials/product.html'
                    })
                    .when('/generator', {
                        controller: 'GeneratorCtrl',
                        templateUrl: 'partials/generator.html'
                    })
                    .otherwise({redirectTo: '/'});
            
        }]);
})(angular.module('case1'));