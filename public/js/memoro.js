$(function() {
    $("#create").click(() => { 
	$.post( "/notes", function( data ) {
	    window.location.href = "/note.html?note=" + data.identificator;
	});
    });
});
