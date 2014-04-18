var services = angular.module('memoroServices', ['ngResource']);

services.factory('User', ['$resource', function($resource) {
  return $resource('api/user/:code');
}]);

services.factory('Board', ['$resource', function($resource) {
  return $resource('api/user/:code/board/:name', {code: '@user', name: '@board'});
}]);

services.factory('Note', ['$resource', function($resource) {
  return $resource('api/user/:code/board/:name/note/:id', {code: '@user', name: '@board', id: '@id'});
}]);
