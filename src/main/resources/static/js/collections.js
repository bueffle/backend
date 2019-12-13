$( document ).ready(function() {

    // var titles = {"collections":[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}]};

    //var titles =[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}];

    if ($("body.collections").length > 0) {
        getAllCollections();
    }


    if ($("body.collection").length > 0) {
        console.log("collection.html");
        loadCollection();
        $("#editCollectionBtn").on("click", editCollection);

        $("#deleteCollectionBtn").on("click", function() {
            deleteCollection(getParameterFromUrlByName('collectionId'))
        });
        getAllCards();
    }
});




function getAllCollections() {
    $.ajax({
        url: "/collections",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            renderCollections(data.content);
        }
    });
}

function createCollection() {
    $.ajax({
        url: "/collections",
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        complete: function(data) {
            $('#createCollectionModal').modal('hide');
            clearCollections();
            getAllCollections();
        },
        data: JSON.stringify({
            "name": $('#formCreateCollectionName').val(),
            "description": $('#formCreateCollectionDescription').val()
        }),
    });
}

function renderCollections(collections) {
    var row = 1;
    for (var i in collections) {
        appendToBody(row,collections[i])
        if(row >= 3) {
            row=1;
        } else {
            row++;
        }
    }
}


function deleteCollection(collection_id) {
    //todo: ask user before

    $.ajax({
        url: "/collections/" + collection_id,
        type: 'DELETE',
        dataType: 'json',
        success: function(data) {
            window.location.href = getBaseUrl() + "/collections.html";
        }
    });
}

function appendToBody(index, collection) {
    console.log("before handlebars collection-template");
    var template = Handlebars.compile($('#collection-template').html());
    console.log("after handlebars collection-template");
    var html = template(collection);
    var addColletionSnipp = $("#create_collection_card").detach();
    $('#nth-column-'+index).append(html);
    (index == 3)?index=1:index++; 
    $('#nth-column-'+index).append(addColletionSnipp);
}

function clearCollections() {
    var addColletionSnipp = $("#create_collection_card").detach();
    $('#nth-column-1').empty();
    $('#nth-column-2').empty();
    $('#nth-column-3').empty();
  
    $('#nth-column-1').append(addColletionSnipp);
    $('#formCreateCollectionName').val("");
    $('#formCreateCollectionDescription').val("");
}

$('#create_new_collection_submit').click(function(event) {
    createCollection()
});

$('#signup_accept').click(function(event){
    var email=$('#signupEmail').val();
    var password1=$('#signupPassword1').val();
    var password2=$('#signupPassword2').val();
    var filter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(filter.test(email)) {
        if(password1==password2) {
            signup({
                "username": email,
                "password": password1,
                "passwordConfirm": password2
            });
        } else {
            $('#signupPassword1').css("background-color","#eb4034");
            $('#signupPassword2').css("background-color","#eb4034");
        }
    } else {
        $('#signupEmail').css("background-color","#eb4034");
    }

});

function signup(data) {

    $.ajax({
        url: "/user",
        type: 'POST',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
          },
        success: function(data) {
            $('#signupModal').modal('hide');
            console.log(data);
            window.open(window.location.origin+"/login","_self");
        },
        data: JSON.stringify(data)
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////
//the same for cards
//todo: gleiche Fkt. nutzen wie oben?

function getAllCards() {
    console.log("getAllCards()");
    url = "/collections/" + getParameterFromUrlByName('collectionId') + "/cards";
    console.log(url);
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log("data of getAllCards(): " + data);
            renderCards(data);
        }
    });
}

function createCard() {
    $.ajax({
        url: "/cards",
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        complete: function(data) {
            $('#createCardModal').modal('hide');
            clearCards();
            getAllCards();
        },
        data: JSON.stringify({
            "name": $('#formCreateCardQuestion').val(),
            "description": $('#formCreateCardAnswer').val()
        }),
    });
}

function renderCards(cards) {
    var row = 1;
    for (var i in cards) {
        appendToBodyCards(row,cards[i])
        if(row >= 3) {
            row=1;
        } else {
            row++;
        }
    }
}

function deleteCard(card_id) {
    $.ajax({
        url: "/cards/" + card_id,
        type: 'DELETE',
        dataType: 'json',
        success: function(data) {
            renderCards(data);
        }
    });
}

function appendToBodyCards(index, card) {
    console.log("before handlebars card-template");
    var template = Handlebars.compile($('#card-template').html());
    console.log("after handlebars card-template");
    var html = template(card);
    var addCardSnipp = $("#create_card").detach();
    $('#nth-column-'+index).append(html);
    (index == 3)?index=1:index++;
    $('#nth-column-'+index).append(addCardSnipp);
}

function clearCards() {
    var addCardSnipp = $("#create_card").detach();
    $('#nth-column-1').empty();
    $('#nth-column-2').empty();
    $('#nth-column-3').empty();

    $('#nth-column-1').append(addCardSnipp);
    $('#formCreateCardQuestion').val("");
    $('#formCreateCardAnswer').val("");
}

$('#create_new_card_submit').click(function(event) {
    createCard()
});


/////////collection.js//////////////

/**
 * Load data of current collection into view (from parameter id from current url)
 * via ajax call on GET /collections/id
 */
function loadCollection() {
    console.log("loadCollection()");

    $.ajax({
        url: getBaseUrl() + "/collections/" + getParameterFromUrlByName('collectionId'),
        type: 'GET',
        //dataType: 'json',
        success: function (data) {
            console.log("description : " + data.description);
            $("#collectionName").html(data.name);
            $("#collectionDescription").html(data.description);

            //Breadcrumb
            var breadCrumbCollection = document.getElementsByClassName("breadcrumb-item")[2];
            breadCrumbCollection.innerHTML = data.name;
            breadCrumbCollection.href = getBaseUrl() + "/collection.html/?collectionId=" + data.id;
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
    var collectionId = getParameterFromUrlByName('collectionId');
    var name = $("#collectionName").text();
    console.log("name: " + name);
    var description = $("#collectionDescription").text();

    //todo: get cards of this collection

    $.ajax({
        url: getBaseUrl() + "/collections/" + collectionId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({"id": collectionId, "name": name, "description": description, "cards": []}),
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
//////////////////////////////////////////////////////////////