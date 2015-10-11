
var readUrl = function (url) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", url, false);
    xmlHttp.send(null);
    return xmlHttp.responseText;
};
var json = JSON.parse(readUrl("../../albums/withReferences"));
function editFileNames(e) {
    var filesToBeUploaded = document.getElementById("filesToBeUploaded");

    while (filesToBeUploaded.firstChild) {
        filesToBeUploaded.removeChild(filesToBeUploaded.firstChild);
    }
    var legendElement = document.createElement("legend");
    var legendElementText = document.createTextNode("Change upload names");
    legendElement.appendChild(legendElementText);
    filesToBeUploaded.appendChild(legendElement);

    var table = document.createElement("table");
    filesToBeUploaded.appendChild(table);

    var thead = document.createElement("thead");
    table.appendChild(thead);

    var trHead = document.createElement("tr");
    thead.appendChild(trHead);
    var originalFilenameHeader = document.createElement("th");
    var originalFilenameHeaderText = document.createTextNode("Uploaded");
    originalFilenameHeader.appendChild(originalFilenameHeaderText);
    trHead.appendChild(originalFilenameHeader);

    var renamedFilenameHeader = document.createElement("th");
    var renamedFilenameHeaderText = document.createTextNode("Title");
    renamedFilenameHeader.appendChild(renamedFilenameHeaderText);
    trHead.appendChild(renamedFilenameHeader);

    var previewFilenameHeader = document.createElement("th");
    var previewFilenameHeaderText = document.createTextNode("Preview");
    previewFilenameHeader.appendChild(previewFilenameHeaderText);
    trHead.appendChild(previewFilenameHeader);

    var tbody = document.createElement("tbody");
    table.appendChild(tbody);


    var files = e.files;
    var quantity = files.length;

    for (var i = 0; i < quantity; i++) {
        var trow = document.createElement("tr");
        tbody.appendChild(trow);
        var file = files.item(i);
        var type = file.type;

        var tdOriginal = document.createElement("td");
        var filename = file.name;
        var tdOriginalText = document.createTextNode(filename);
        tdOriginal.appendChild(tdOriginalText);
        trow.appendChild(tdOriginal);
        var dotSplitFilename = filename.split('.');
        if(dotSplitFilename.length > 1) {
            dotSplitFilename.pop();
            dotSplitFilename = dotSplitFilename.join('.');
        }
        else {
            dotSplitFilename = dotSplitFilename[0];
        }
        var inputElement = document.createElement("input");
        inputElement.setAttribute("name", "file." + filename);
        inputElement.setAttribute("type", "text");
        inputElement.setAttribute("required", "true");

        inputElement.setAttribute("value", dotSplitFilename);
        var tdRenamed = document.createElement("td");
        tdRenamed.appendChild(inputElement);
        trow.appendChild(tdRenamed);

        var tdPreview = document.createElement("td");
        trow.appendChild(tdPreview);
        if (type.indexOf("image") === 0) {
            var imageElement = document.createElement("img");
            imageElement.style.maxWidth = "100px";
            imageElement.style.maxHeight = "100px";
            tdPreview.appendChild(imageElement);
            (function (f, i) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    i.src = e.target.result;
                };
                reader.readAsDataURL(f);
            })(file, imageElement);

        }

    }
}
function addTag() {
    var divElement = document.createElement("div");


    var inputElement = document.createElement("input");
    inputElement.setAttribute("name", "tags");
    inputElement.setAttribute("type", "text");

    inputElement.setAttribute("required", "true");

    var labelElement = document.createElement("label");
    labelElement.appendChild(inputElement);
    var removeElement = document.createElement("input");
    removeElement.setAttribute("type", "button");
    removeElement.setAttribute("value", "Remove Tag");
    removeElement.onclick = function () {
        divElement.parentNode.removeChild(divElement);
    };


    divElement.appendChild(labelElement);
    divElement.appendChild(removeElement);
    document.getElementById("tagPlaceholder").appendChild(divElement);
}
var selectElement = document.createElement("select");
selectElement.setAttribute("multiple","");
selectElement.setAttribute("name","albums");
var albums = json['albums'];
for (var i=0;i<albums.length;i++) {
    var currentObject = albums[i]['object']
    var name = currentObject['name'];
    var optionElement = document.createElement("option");
    optionElement.setAttribute("value",albums[i]['databaseReference']);
    var textElement = document.createTextNode(name);
    optionElement.appendChild(textElement);
    selectElement.appendChild(optionElement);albums[0]
}
var divElement = document.createElement("div");
document.getElementById("albumPlaceholder").appendChild(selectElement);


var form = document.getElementById("uploadFiles");
function sendData() {
    var XHR = new XMLHttpRequest();
    var FD = new FormData(form);
    XHR.addEventListener("load", function () {
        alert("Added " + this.responseText);
    });
    XHR.addEventListener("error", function () {
        alert('Oups! Something goes wrong.');
    });
    XHR.open("POST", "../../raw");
    XHR.send(FD);
}




form.addEventListener("submit", function (event) {
    event.preventDefault();

    sendData();
});