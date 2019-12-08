$(function() {
    if ($("body.index").length > 0) {
        console.log("index.html");
        getCollections();
    }

    if ($("body.collection").length > 0) {
        console.log("collection.html");
        loadCollection();
        $("#editCollectionBtn").on("click", editCollection);
    }
});

/**
 * Get collections by ajax call
 */
function getCollections() {
    console.log("getCollections()");
    var url = $(location).attr('origin'); //base url, instead of href
    console.log("url: " + url);

    $.ajax({
        url: url + "/collections",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log("first value: " + data[0].name);
            loadCollections(data);
        }
    });

    /* Alternative:
    $.get( url + "/collections", function(data) {
        console.log(data[0].name);
        loadCollections(data);
    });*/
}

/**
 * Load data of collections (response from GET to /collections) into view
 * @param data
 */
function loadCollections(data) {
    console.log("loadCollections(data)");
    var collectionDescriptions = document.getElementsByClassName("card-text");
    for (num in data) {
        //parent
        var row = $(".row");

        //appendChild with JQuery?
        /*row.append(
              '<div class="col-md-4">'
            +   '<div class="card mb-4 shadow-sm">'
            +     '<img class="bd-placeholder-img card-img-top" src="https://images.unsplash.com/photo-1544642058-c5d172ab955c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" />'
            +     '<div class="card-body">'
            +       '<p class="card-text"' + '</p>'
            //+       '<div class="d-flex justify-content-between align-items-center">'
            //+         '<div class="btn-group">'
            //+           '<a type="button" class="btn btn-sm btn-outline-secondary" value="Learn"</a>'
            //+           '<a type="button" class="btn btn-sm btn-outline-secondary" value="Edit"</a>'
            //+         '</div>'
            //+         '<small class="text-muted"</small>'
            //+       '</div>'
            +     '</div>'
            +   '</div>'
            + '</div>'
        );*/

        //$(".card-text:eq(num)").innerText = (data[num].description);
        //$(".card-text:eq(num)").html(data[num].description);
        //$(".card-text:eq(num)").text(data[num].description);

        //todo: set name of collection
        collectionDescriptions[num].innerHTML = data[num].description;

        //edit button
        var element = document.getElementsByClassName("btn-group")[num].children[1];
        element.innerHTML = "editieren";
        var url = $(location).attr('origin'); //base url, instead of href
        element.href = url + "/collection.html/?id=" + data[num].id;
        console.log(url + "/collection.html/?id=" + data[num].id);
    }
}

/**
 * Load data of current collection (from parameter id from current url) via ajax call on GET /collections/id into view
 */
function loadCollection() {
    console.log("loadCollection()");
    var url = $(location).attr('origin'); //base url, instead of href
    console.log("url: " + url);

    //get parameter id from current url
    var id = new URLSearchParams(window.location.search).get('id');
    console.log("idFromUrl: " + id);

    $.ajax({
        url: url + "/collections/" + id,
        type: 'GET',
        //dataType: 'json',
        success: function (response) {
            console.log("Beschreibung ist : " + response.description);
            $("#collectionName").html(response.name);
            $("#collectionDescription").html(response.description);
        }
    });
}

/**
 * Edit button was clicked: make collection editable, change buttons
 */
function editCollection() {
    console.log("editCollection()");
    $(this).hide();
    $("#collectionName").attr("contenteditable", "true");
    $("#collectionName").css("background-color", "white");
    $("#collectionDescription").attr("contenteditable", "true");
    $("#collectionDescription").css("background-color", "white");
    $("#collectionBtn1").html("Save");
    $("#collectionBtn2").html("Cancel");
    //cancel
    $("#collectionBtn2").on("click", setCollectionUneditable);
    //save
    $("#collectionBtn1").on("click", function() {
        setCollectionUneditable();
        //todo: save changes via POST/PUT
    });
}

/**
 * Set elements of collection.html from edit mode back to normal mode
 */
function setCollectionUneditable() {
    console.log("setCollectionUneditable()");
    $("#editCollectionBtn").show();
    $("#collectionName").removeAttr('contenteditable style');
    $("#collectionDescription").removeAttr('contenteditable style');
    //todo: buttons
    $("#collectionBtn1").html("New Card");
    $("#collectionBtn2").html("Start Training");
}

