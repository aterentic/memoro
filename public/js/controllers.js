// TODO Global scope, move to module scope.
function array(nodes) {
  return Array.prototype.slice.call(nodes);
}

function param(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function select(element) {
  array(element.parentNode.querySelectorAll("li")).forEach(function(child) {
    if (element == child) {
      return true;
    }
    child.classList.remove("selected");
  });
  element.classList.add("selected");
}

function getSelectedBoard() {
  return document.querySelector("li.board.selected").getAttribute("data-name");
}

var memoro = angular.module('memoro', []);

memoro.controller('UsersController', function ($scope, $http) {

  $http.get('api/users').success(function(data) {
    $scope.users = data;
  });

});

memoro.controller('UserController', function ($scope, $http) {

  $scope.orderBy = "id";

  $http.get('api/user/' + param("code")).success(function(data) {
    $scope.user = data;
    if(data.boards) {
      $scope.board = data.boards[0];
      $scope.board.selected = " selected";
    }
  });

  $scope.boardClick = function($event) {
    select($event.target);
    $http.get('/api/user/' + param("code") + "/board/" + getSelectedBoard()).success(function(data) {
      $scope.board = data;
    });
  };

  $scope.newBoardText = "Add new board...";

  $scope.newBoardKeydown = function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
      if (!event.target.classList.contains("empty")) {
        $http.post('/api/boards', { "user": param("code"), "name": event.target.innerHTML }).success(function(data) {
          $scope.user = data;
          event.target.classList.add("empty");
          event.target.innerHTML = $scope.newBoardText;
          event.target.contentEditable = false;
          $scope.board = data.boards[0];
          $scope.board.selected = " selected";
        });
      }
      return false;
    }
  };

  $scope.newBoardKeypress = function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
    }
  };

  $scope.newBoardClick = function(event) {
    if (event.target.classList.contains("empty")) {
      event.target.innerHTML  = "";
      event.target.classList.remove("empty");
      event.target.contentEditable = true;
      event.target.focus();
    }
  };

  $scope.newBoardBlur = function(event) {
    if (event.target.innerHTML == "") {
      event.target.classList.add("empty");
      event.target.innerHTML = $scope.newBoardText;
      event.target.contentEditable = false;
    }
  };

  $scope.newNoteText = "Add new note...";

  $scope.newNoteKeydown =function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
      if (!event.target.classList.contains("empty")) {
        $http.post('/api/notes', { "user": param("code"), "board" : getSelectedBoard(), "text": event.target.innerHTML }).success(function(data) {
          $scope.board = data;
          event.target.classList.add("empty");
          event.target.innerHTML = $scope.newNoteText;
          event.target.contentEditable = false;
        });
      }
      return false;
    }
  };

  $scope.newNoteKeypress = function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
    }
  };

  $scope.newNoteClick = function(event) {
    if (event.target.classList.contains("empty")) {
      event.target.innerHTML  = "";
      event.target.classList.remove("empty");
      event.target.contentEditable = true;
      event.target.focus();
    }
  };

  $scope.newNoteBlur = function(event) {
    if (event.target.innerHTML == "") {
      event.target.classList.add("empty");
      event.target.innerHTML = $scope.newNoteText;
      event.target.contentEditable = false;
    }
  };

  $scope.noteClick = function($event) {
    select($event.target);
  };

});
