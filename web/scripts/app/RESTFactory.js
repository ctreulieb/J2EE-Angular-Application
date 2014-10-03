(function (app) {
    // call REST functions (JAX-RS) on Glassfish Server
    //   action = get, post, put, delete
    //   url = path on server
    //   id = optional parameter
    //   entity = payload for add or update
    //   similar to RESTService.js only using FACTORY syntax
    var RESTFactory = function ($http) {

        RESTFactory.restCall = function (action, url, id, entity) {

            switch (action) {
                case 'get':
                    if (id > 0 ) {
                        url = url + '/' + id;
                    }
                    return $http.get(url).then(function (result) {
                        return result.data;
                    }, function (reason) { //error
                        return reason.status;
                    });
                    break;
                case 'post':
                    return $http.post(url + '/', entity).then(function (result) {
                        return result.data;
                    }, function (reason) { //error
                        return reason.status;
                    });
                    break;
                case 'put':
                    return $http.put(url + '/', entity).then(function (result) {
                        return result.data;
                    }, function (reason) { //error
                        return reason.status;
                    });
                    break;
                case 'delete':
                    return $http.delete(url + '/' + id).then(function (result) {
                        return result.data;
                    }, function (reason) { //error
                        return reason.status;
                    });
                    break;
            }
        };

        return RESTFactory;
    };

    app.factory('RESTFactory', ['$http', RESTFactory]);

})(angular.module('case1'));