var services = angular.module('memoroServices', ['ngResource']);

services.factory('Users', ['$resource', function($resource) {
  return $resource('api/users', {}, {
    query: {method:'GET', isArray:true}
  });
}]);

services.factory('User', ['$resource', function($resource) {
  return $resource('api/user/:code', {}, {
    get : { method:'GET', params: { "code" : "code" }}
  });
}]);

services.factory('Board', ['$resource', function($resource) {
  return $resource('api/user/:code/board/:name', {}, {
    get : { method:'GET', params: { "code" : "code", "name" : "name" }}
  });
}]);
