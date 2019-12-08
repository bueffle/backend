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

    $.ajax({
        url: getBaseUrl() + "/collections",
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
        var editBtn = document.getElementsByClassName("btn-group")[num].children[1];
        editBtn.innerHTML = "editieren";
        editBtn.href = getBaseUrl() + "/collection.html/?id=" + data[num].id;
        console.log(getBaseUrl() + "/collection.html/?id=" + data[num].id);
    }
}

/**
 * Load data of current collection (from parameter id from current url) via ajax call on GET /collections/id into view
 */
function loadCollection() {
    console.log("loadCollection()");

    $.ajax({
        url: getBaseUrl() + "/collections/" + getParameterFromUrlByName('id'),
        type: 'GET',
        //dataType: 'json',
        success: function (data) {
            console.log("description : " + data.description);
            $("#collectionName").html(data.name);
            $("#collectionDescription").html(data.description);

            //Breadcrumb
            var breadCrumbCollection = document.getElementsByClassName("breadcrumb-item")[1];
            breadCrumbCollection.innerHTML = data.name;
            breadCrumbCollection.href = getBaseUrl() + "/collection.html/?id=" + data.id;
            //getBaseUrl() necessary?
            console.log("breadCrumbCollection.href: " + breadCrumbCollection.href);
        }
    });


}

/**
 * Update changes of collection by PUT to /collections/id
 */
function updateCollection() {
    console.log("updateCollection()");
    var id = getParameterFromUrlByName('id');
    var name = $("#collectionName").text();
    console.log("name: " + name);
    var description = $("#collectionDescription").text();

    //todo: get cards of this collection

    $.ajax({
        url: getBaseUrl() + "/collections/" + id,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({"id": id, "name": name, "description": description, "cards": []}),
        success: function (data) {
            console.log(data);
            console.log('process success');
        },
    });
}

/**
 * Edit button was clicked: make collection editable, change buttons
 */
function editCollection() {
    console.log("editCollection()");
    $(this).hide();
    var name = $("#collectionName").text();
    $("#collectionName").attr("contenteditable", "true");
    $("#collectionName").css("background-color", "white");
    var description = $("#collectionDescription").text();
    $("#collectionDescription").attr("contenteditable", "true");
    $("#collectionDescription").css("background-color", "white");
    $("#collectionBtn1").html("Save");
    $("#collectionBtn2").html("Cancel");
    //cancel
    $("#collectionBtn2").on("click", function() {
        setCollectionUneditable();
        $("#collectionName").text(name);
        $("#collectionDescription").text(description);
    });
    //save
    $("#collectionBtn1").on("click", function() {
        setCollectionUneditable();
        updateCollection();

        //Breadcrumb
        var breadCrumbCollection = document.getElementsByClassName("breadcrumb-item")[1];
        breadCrumbCollection.innerHTML =  $("#collectionName").text();
    });
}

//todo: save new collection by POST

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

/**
 * Get parameter by given name from current URL
 * @param paramName name of parameter (e.g.: ?id=)
 * @returns {string}
 */
function getParameterFromUrlByName(paramName) {
    var  urlParams= new URLSearchParams(window.location.search);
    var param = urlParams.get(paramName);
    console.log("getParameterFromUrlByName(" + paramName + "): " + param);
    return param;
}

/**
 * Return base part of URL (e.g.: )
 * @returns {string | jQuery}
 */
function getBaseUrl() {
    var url = $(location).attr('origin');
    console.log("getBaseUrl(): " + url);
    return url;
}

