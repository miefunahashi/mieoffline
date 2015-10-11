var readUrl = function(url) {
    var xmlHttp = null;
    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", url, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
};


var json = JSON.parse(readUrl("../../../raw/files"));
var template = Handlebars.compile(readUrl("uploads.hbs"));
var output = template(json);
document.getElementById("placeholder").innerHTML=output;