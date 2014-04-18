var services = angular.module('memoroServices', ['ngResource']);

services.factory('User', ['$resource', function($resource) {
  return $resource('api/user/:code');
}]);

services.factory('Board', ['$resource', function($resource) {
  return $resource('api/user/:code/board/:name');
}]);
