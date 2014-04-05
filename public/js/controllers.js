
// TODO Global scope, move to module scope.
function select(selector, element) {
  return Array.prototype.slice.call((element == null ? document : element).querySelectorAll(selector));
}

function param(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

var memoro = angular.module('memoro', []);

memoro.directive('memoroSelectable', function () {

  return {
    restrict: 'A',
    link: function (scope, element, attribute) {
      element.on("click", function(event) {
        select("#boards .board").forEach(function(element) {
          if (event.target == element) {
            return true;
          }
          element.classList.remove("selected");
        });
        event.target.classList.add("selected");
      });
    }
  };

});

memoro.controller('UsersController', function ($scope, $http) {

  $http.get('api/users').success(function(data) {
    $scope.users = data;
  });

});

memoro.controller('UserController', function ($scope, $http) {

  $http.get('api/user/' + param("code")).success(function(data) {
    $scope.user = data;
  });

  var text = "Add new board...";

  var board = document.getElementById("new-board");
  board.innerHTML = text;
  board.classList.add("empty");

  board.addEventListener("keydown", function(event) {
    if (event.keyCode == 13) {
      if (!event.target.classList.contains("empty")) {
        $http.post('/api/boards', { "user": param("code"), "name": event.target.innerHTML }).success(function(data) {
          $scope.user = data;
          event.target.classList.add("empty");
          event.target.innerHTML = text;
          event.target.contentEditable = false;
        });
      }
      event.preventDefault();
    }
  });

  board.addEventListener("click", function(event) {
    if (event.target.classList.contains("empty")) {
      event.target.innerHTML  = "";
      event.target.classList.remove("empty");
      event.target.contentEditable = true;
      event.target.focus();
    }
  });

  board.addEventListener("blur", function(event) {
    if (event.target.innerHTML == "") {
      event.target.classList.add("empty");
      event.target.innerHTML = text;
      event.target.contentEditable = false;
    }
  });

});
