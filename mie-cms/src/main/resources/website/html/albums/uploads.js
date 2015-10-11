var readUrl = function(url) {
    var xmlHttp = null;
    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", url, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
};


var json = JSON.parse(readUrl("../../../albums/basic"));
var template = Handlebars.compile(readUrl("uploads.hbs"));
var output = template(json);
document.getElementById("placeholder").innerHTML=output;


function sendData() {
    var form = document.getElementById("addAlbum");
    var XHR = new XMLHttpRequest();
    var FD = new FormData(form);
    XHR.addEventListener("load", function () {
       location.reload();
    });
    XHR.addEventListener("error", function () {
        alert('Oups! Something goes wrong.');
    });
    XHR.open("POST", "../../../albums");
    XHR.send(FD);
}
