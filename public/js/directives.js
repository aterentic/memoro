var directives = angular.module('memoroDirectives', []);

directives.directive('memoroDraggable', ['$document', function($document) {

  return function(scope, element, attr) {

    var move = false;
    var prevousX = 0;

    element.on('mousedown', function(event) {
      prevousX = event.clientX;
      move = true;
      event.preventDefault();
    });

    element.on('mouseup', function(event) {
      move = false;
    });

    element.on('mouseout', function(event) {
      move = false;
    });

    element.on('mousemove', function(event) {
      if (!move) return false;
      element[0].scrollLeft -= event.clientX - prevousX;
      prevousX = event.clientX;
    });

    element.on('mousedown', function(event) {
      event.preventDefault();
    });

  }

}]);
