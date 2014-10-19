var memoro = (function () {

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
