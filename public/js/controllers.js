var memoro = angular.module('memoro', []);

memoro.controller('UsersController', function ($scope, $http) {
  $http.get('api/users').success(function(data) {
    $scope.users = data;
  });
});
