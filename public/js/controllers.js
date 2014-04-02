var memoro = angular.module('memoro', []);

memoro.controller('UsersController', function ($scope, $http) {
  $http.get('api/users').success(function(data) {
    $scope.users = data;
  });
});

memoro.controller('UserController', function ($scope, $http) {

  var text = "Add new board...";

  var board = document.getElementById("new-board");

  board.innerHTML = text;
  board.classList.add("empty");

  board.addEventListener("click", function(event) {
    if (event.target.classList.contains("empty")) {
      event.target.innerHTML  = "";
      event.target.classList.remove("empty");
    }
    event.target.contentEditable = true;
    event.target.focus();
  });

  board.addEventListener("blur", function(event) {
    if (event.target.innerHTML == "") {
      event.target.classList.add("empty");
      event.target.innerHTML = text;
    } else {
      alert("TODO: Add board to user.");
    }
  });

});
