var services = angular.module('memoroServices', ['ngResource']);

services.factory('Users', ['$resource', function($resource) {
  return $resource('api/users', {}, {
    query: {method:'GET', isArray:true}
  });
}]);
