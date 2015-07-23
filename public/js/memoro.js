location.queryParams = {};

(function(queryString) {
    if (queryString === "") return;
    if (queryString.substr(1) === "") return;
    queryString.substr(1).split("&").forEach(function (pair) {
	if (pair === "") return;
	var parts = pair.split("=");
	location.queryParams[parts[0]] = parts[1] &&
            decodeURIComponent(parts[1].replace(/\+/g, " "));
    });
})(location.search);

$(function() {  
    $("#create").click(() => { 
	$.post( "/notes", function( data ) {
	    window.location.href = "/note.html?note=" + data.identificator;
	});
    });
});
