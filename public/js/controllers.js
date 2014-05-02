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

controllers.controller('UsersController', ['$scope', 'User', function ($scope, User) {
  $scope.users = User.query();
}]);

controllers.controller('UserController', ['$scope', 'User', 'Board', 'Note', function ($scope, User, Board, Note) {

  var move = false;
  var prevousX = 0;

  $scope.boardsMouseDown = function($event) {
    prevousX = $event.clientX;
    move = true;
    $event.preventDefault();
  }

  $scope.boardsMouseUp = function($event) {
    move = false;
  }

  $scope.boardsMouseOut = function($event) {
    move = false;
  }

  $scope.boardsMouseMove = function($event) {
    if (!move) return false;
    document.querySelector("#boards").scrollLeft -= $event.clientX - prevousX;
    prevousX = $event.clientX;
  }

  $scope.boardMouseDown = function($event) {
    $event.preventDefault();
  }

  function makeEditable(element) {
    element.classList.remove("empty");
    element.classList.add("edit");
    element.contentEditable = true;
    element.focus();
    element.innerHTML  = "";
  }

  function makeEmpty(element, tip) {
    element.classList.remove("edit");
    element.classList.add("empty");
    element.innerHTML = tip;
    element.contentEditable = false;
  }

  function isEmpty(element) {
    return element.classList.contains("empty");
  }

  $scope.orderBy = "id";

  $scope.user = User.get({"code" : param("code")}, function(user) {
    if (user.boards) {
      $scope.board = user.boards[0];
      $scope.board.selected = " selected";
    };
  });

  $scope.boardClick = function($event) {
    select($event.target);
    $scope.board = Board.get({ "code" : param("code"), "name" : getSelectedBoard()});
  };

  $scope.newBoardText = "Add new board...";

  $scope.newBoardKeydown = function(event) {
    if (event.keyCode != 13) {
      return true;
    }
    event.preventDefault();
    if (!isEmpty(event.target)) {
      new Board({ "user" : param("code"), "name" : event.target.innerHTML }).$save(function(board) {
        User.get({ "code" : param("code") }, function(user) {
          $scope.user = user;
          array(user.boards).forEach(function(candidate) {
            if (candidate.name == board.name) {
              $scope.board = candidate;
              $scope.board.selected = " selected";
              return false;
            }
          });
          makeEmpty(event.target, $scope.newBoardText);
        });
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
      new Note({ "user": param("code"), "board" : getSelectedBoard(), "text": event.target.innerHTML }).$save(function(note) {
        makeEmpty(event.target, $scope.newNoteText);
        $scope.board = Board.get({ "code": param("code"), "name" : getSelectedBoard()});
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

}]);
