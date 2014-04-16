var services = angular.module('memoroServices', ['ngResource']);

services.factory('User', ['$resource', function($resource) {
  return $resource('api/user/:code', {}, {
    query: { method: 'GET', isArray:true},
    get : { method: 'GET' }
  });
}]);

services.factory('Board', ['$resource', function($resource) {
  return $resource('api/user/:code/board/:name', {}, {
    get : { method: 'GET' }
  });
}]);
