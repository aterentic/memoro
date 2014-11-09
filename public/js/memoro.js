(function () {

  function store(key, val) {
    localStorage.setItem(key, val);
  }

  function read(key) {
    return localStorage.getItem(key);
  }

  return {
    id : function() {
      return 'xxxxxxxxxx'.replace(/./g, function(c) {
        return Math.floor(Math.random() * 16).toString(16);
      });
    },
    user : function(id) {
      return id ? store("user", id) : read("user");
    }
  }

})();

var memoro = angular.module('memoro', ['ngRoute', 'ngResource']);

memoro.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $routeProvider.
  when('/start', {
    templateUrl: 'start.html',
    controller: 'StartController'
  }).
  otherwise({
    redirectTo: '/start'
  });
  $locationProvider.html5Mode({"enabled" : false, "requireBase" : false});
}]);


memoro.factory('Memoro', ['$resource', function($resource) {
  return $resource('memoro');
}]);

memoro.controller('MemoroController', ['$scope', 'Memoro', function($scope, Memoro) {
  Memoro.get({}, function(memoro) {
    console.log(memoro);
  });
}]);

memoro.controller('StartController', function($scope) {
  console.log("Welcome to Memoro.");
});

