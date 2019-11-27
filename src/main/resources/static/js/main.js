<<<<<<< HEAD
var collections=jQuery.get('/collections',function(data){console.log(data);})


var source = document.getElementById("entry-template").innerHTML;
var template = Handlebars.compile(source);


for (var collection in collections ){

    var context = { title: collection.name };
    var html = template(context);


    $( "#collections-content" ).append( html );
=======
$(function() {
    getCollections();
    $("#editCollectionBtn").on("click", editCollection);

});

/**
 * Edit button was clicked: make collection editable, change buttons
 */
function editCollection() {
    $(this).hide();
    $("#collectionTitle").attr("contenteditable", "true");
    $("#collectionTitle").css("background-color", "white");
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
    $("#editCollectionBtn").show();
    $("#collectionTitle").removeAttr('contenteditable style');
    $("#collectionDescription").removeAttr('contenteditable style');
    //todo: buttons
    $("#collectionBtn1").html("New Card");
    $("#collectionBtn2").html("Start Training");
}

/**
 * Get collections by ajax call
 */
function getCollections() {
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

        collectionDescriptions[num].innerHTML = data[num].description;
    }
>>>>>>> e6fd945be5cca64c2f7474979d2474779811e60b
}