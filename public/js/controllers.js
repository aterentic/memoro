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

var controllers = angular.module('memoroControllers', []);

controllers.controller('UsersController', ['$scope', 'Users', function ($scope, Users) {
  $scope.users = Users.query();
}]);

controllers.controller('UserController', function ($scope, $http) {

  function makeEditable(element) {
    element.innerHTML  = "";
    element.classList.remove("empty");
    element.contentEditable = true;
    element.focus();
  }

  function makeEmpty(element, tip) {
    element.classList.add("empty");
    element.innerHTML = tip;
    element.contentEditable = false;
  }

  function isEmpty(element) {
    return element.classList.contains("empty");
  }

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
    if (event.keyCode != 13) {
      return true;
    }
    event.preventDefault();
    if (!isEmpty(event.target)) {
      $http.post('/api/boards', { "user": param("code"), "name": event.target.innerHTML }).success(function(data) {
        makeEmpty(event.target, $scope.newBoardText);
        $scope.user = data;
        $scope.board = data.boards[0];
        $scope.board.selected = " selected";
      });
    }
    return false;
  };

  $scope.newBoardKeypress = function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
    }
  };

  $scope.newBoardClick = function(event) {
    if (isEmpty(event.target)) {
      makeEditable(event.target);
    }
  };

  $scope.newBoardBlur = function(event) {
    if (event.target.innerHTML == "") {
      makeEmpty(event.target, $scope.newBoardText);
    }
  };

  $scope.newNoteText = "Add new note...";

  $scope.newNoteKeydown =function(event) {
    if (event.keyCode != 13) {
      return true;
    }
    event.preventDefault();
    if (!isEmpty(event.target)) {
      $http.post('/api/notes', { "user": param("code"), "board" : getSelectedBoard(), "text": event.target.innerHTML }).success(function(data) {
        makeEmpty(event.target, $scope.newNoteText);
        $scope.board = data;
      });
    }
    return false;
  };

  $scope.newNoteKeypress = function(event) {
    if (event.keyCode == 13) {
      event.preventDefault();
    }
  };

  $scope.newNoteClick = function(event) {
    if (event.target.classList.contains("empty")) {
      makeEditable(event.target);
    }
  };

  $scope.newNoteBlur = function(event) {
    if (event.target.innerHTML == "") {
      makeEmpty(event.target, $scope.newNoteText);
    }
  };

  $scope.noteClick = function($event) {
    select($event.target);
  };

});
