/**
 * Collection(s) Javascript file
 *
 * A collection of shared functions in context of collections and cards in collections
 *
 * @link   https://github.com/bueffle/backend/blob/master/src/main/resources/static/js/collections.js
 * @author AuthorName.
 */


 /**
  * Instantly executing function to determine which load function should be executed 
  */
$(function () {
    if ($("body.collections").length > 0) {
        getAllCollections();
    }
    if ($("body.collection").length > 0) {
        loadCollection();
    }
});


/**
 * Retrieves all collections depending on the context of the call. 
 * If a search string is set (e.g. page.html?search=text) the request will switch to search mode
 * If a the context is on mycollections.html the request will return only the users collections
 * 
 */
function getAllCollections() {
    if (window.location.pathname == "/mycollections.html") {
        request_url = "/collections/own";
    } else {
        var search = parseSearch("search");
        if (search && "search" in search) {
            request_url = "/collections?name=" + search["search"];
            //$('#collection_search').val(search["search"]); template is not loaded yet
        } else {

            request_url = "/collections";
        }
    }
    $.ajax({
        url: request_url,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            renderCollections(data.content);
        }
    });
}

/**
 * Creates a collection by extracting the required params directly from the input elements
 * After the request is completed the collections are reloaded 
 */
function createCollection() {
    $.ajax({
        url: "/collections",
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
            console.log(data);
        },
        complete: function (data) {
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

/**
 * Determines the colum that the collection should be appended to 
 * 
 * @param {Object} collections 
 */
function renderCollections(collections) {
    var row = 1;
    for (var i in collections) {
        appendToBody(row, collections[i])
        if (row >= 3) {
            row = 1;
        } else {
            row++;
        }
    }
}

/**
 * Creates a learning session and writes the information into a cookie, overwriting any existing cookie
 * This information is used in learn.html context to set the learn session.
 * 
 * @param {Int} collection_id 
 */
function learnCollection(collection_id) {
    var settings = {
        url: "/collections/" + collection_id + "/learn",
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify({
            learningRunPlus: true
        }),
        success: function (resp) {
            createCookie("learnrun", JSON.stringify({
                collection_id: collection_id,
                learnrun: resp.id
            }), 1)
            window.location.href = window.location.origin + "/learn.html";
        }
    }

    $.ajax(settings).done(function (response) {
        console.log(response);
    });
}

/**
 * Deletes collection
 * todo: ask user before, also delete references on cards?
 * 
 * @param collection_id
 */
function deleteCollection(collection_id) {
    $.ajax({
        url: "/collections/" + collection_id,
        type: 'DELETE',
        dataType: 'json',
        complete: function (data) {
            console.log("DELETE /collections/" + collection_id);
            //redirect back to collections is not possible here
        }
    });
    window.location.href = "mycollections.html";
}


/**
 * Retrieves the template, parses the information and appends the html to the given index colum 
 * 
 * @param {Int} index 
 * @param {Object} collection 
 */
function appendToBody(index, collection) {
    var template = Handlebars.compile($('#collection-template').html());
    var html = template(collection);
    var addColletionSnipp = $("#create_collection_card").detach();
    $('#nth-column-' + index).append(html);
    (index == 3) ? index = 1: index++;
    $('#nth-column-' + index).append(addColletionSnipp);
}


/**
 * Removes all collection elements from the DOM
 */
function clearCollections() {
    var addColletionSnipp = $("#create_collection_card").detach();
    $('#nth-column-1').empty();
    $('#nth-column-2').empty();
    $('#nth-column-3').empty();

    $('#nth-column-1').append(addColletionSnipp);
    $('#formCreateCollectionName').val("");
    $('#formCreateCollectionDescription').val("");
}


/**
 * On-click listener to create a new collection
 */
$('#create_new_collection_submit').click(function (event) {
    createCollection()
});



/////////////////////////////////////////////////////////////////////////////////////////////
//functions for cards
/////////////////////////////////////////////////////////////////////////////////////////////

function getAllCards() {
    console.log("getAllCards()");
    url = "/collections/" + getParameterFromUrlByName('collectionId') + "/cards";
    console.log(url);
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
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
        complete: function (data) {

            $.ajax({
                url: "/cards/" + data.responseJSON.id + "/collections/" + getParameterFromUrlByName('collectionId'),
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                complete: function (data) {

                    $('#createCardModal').modal('hide');
                    clearCards();
                    getAllCards();
                }
            });
        },
        data: JSON.stringify({
            "question": $('#formCreateCardQuestion').val(),
            "answer": $('#formCreateCardAnswer').val()
        }),
    });
}

function renderCards(cards) {
    var row = 1;
    for (var i in cards) {
        appendToBodyCards(row, cards[i])
        if (row >= 3) {
            row = 1;
        } else {
            row++;
        }
    }
}

function deleteCard(card_id) {
    console.log("deleteCard(" + card_id + ")");
    $.ajax({
        url: "/cards/" + card_id,
        type: 'DELETE',
        dataType: 'json',
        success: function (data) {
            console.log("data of deleteCard(): " + data);
            //renderCards(data);
        }
    });
    location.reload();
}

/**
 * Loads the modal required for editing a card
 * @param {*} card_id 
 */
function editCard(card_id) {
    $("#js_modal_holder").empty();
    var url = "/collections/" + getParameterFromUrlByName('collectionId') + "/cards";

    var settings = {
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            var card_data = null;
            for (var i = 0; i < data.length; i++) {
                if (data[i]["id"] == card_id) {
                    card_data = data[i];
                    break;
                }
            }
            if (card_data) {
                getTemplateAjax("/templates/edit-card.handlebars", "js_modal_holder", card_data, function () {
                    $('#editCardModal').modal();
                });
            } else {
                console.error("card data not found");
            }
        }
    }
    $.ajax(settings);




}


/**
 * Create or update a card
 * If the card does not have card_public input, it is set to true
 * @param {String} card_id 
 */
function saveCard(card_id) {

    var card_data = {
        question: $("#card_question").val(),
        answer: $("#card_answer").val(),
        public: $("#card_public").is(":checked") || true
    }

    $.ajax({
        url: "/cards/" + card_id,
        type: 'PUT',
        contentType: 'application/json',
        dataType: 'json',
        complete: function (data) {
            $('#editCardModal').modal('hide');
            location.reload();
        },
        data: JSON.stringify(card_data),
    });

    console.info(card_data);

}

function appendToBodyCards(index, card) {
    var template = Handlebars.compile($('#card-template').html());
    var html = template(card);
    var addCardSnipp = $("#create_card").detach();
    $('#nth-column-' + index).append(html);
    (index == 3) ? index = 1: index++;
    $('#nth-column-' + index).append(addCardSnipp);
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

$('#create_new_card_submit').click(function (event) {
    createCard();
});

/////////collection.js//////////////

/**
 * Saves the current collection
 * incl. changes of buttons and breadcrumbs
 */
function saveCollection() {
    setCollectionUneditable();
    updateCollection();
    var breadCrumbCollection = $(".breadcrumb-item").last().find("a")[0];
    breadCrumbCollection.innerHTML = $("#collectionName").text();
}

/**
 * Load data of current collection into view (from parameter id from current url)
 * via ajax call on GET /collections/id and also loads cards
 */
function loadCollection() {
    console.log("loadCollection()");
    $("#editCollectionBtn").on("click", editCollection);
    $("#saveCollectionBtn").on("click", saveCollection);

    //modal for confirming the delete of the collection
    $('#confirmDeleteCollectionModal').on('show.bs.modal', function (e) {
        $('#confirmDeleteCollection').on("click", function () {
            deleteCollection(getParameterFromUrlByName('collectionId'));
        })
    });

    //modal for confirming the delete of the cards
    $('#confirmDeleteCardModal').on('show.bs.modal', function (e) {
        var cardId = e.relatedTarget.dataset['id'];
        console.log("deleteCard: " + cardId);
        $('#confirmDeleteCard').on("click", function () {
            deleteCard(cardId);
        })
    });

    $.ajax({
        url: "/collections/" + getParameterFromUrlByName('collectionId'),
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log("description : " + data.description);
            $("#collectionName").html(data.name);
            $("#collectionDescription").html(data.description);
            $('#collection_public_state option[value="' + data.public + '"]').prop("selected", true)

            //Breadcrumb
            var breadCrumbCollection = $(".breadcrumb-item").last().find("a")[0];
            breadCrumbCollection.innerHTML = data.name;
            breadCrumbCollection.href = "collection.html/?collectionId=" + data.id;
            console.log("breadCrumbCollection.href: " + breadCrumbCollection.href);
        }
    });
    getAllCards();
}

/**
 * Update changes of collection by PUT to /collections/id
 * Cards are saved to collection when created or edited individually
 */
function updateCollection() {
    console.log("updateCollection()");
    var collectionId = getParameterFromUrlByName('collectionId');
    var name = $("#collectionName").text();
    console.log("name: " + name);
    var description = $("#collectionDescription").text();
    var public_state = $("#collection_public_state").children("option:selected").val()

    $.ajax({
        url: "/collections/" + collectionId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            "id": collectionId,
            "name": name,
            "description": description,
            "public": public_state
        }),
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
    document.getElementById("saveCollectionBtn").disabled = false;
    document.getElementById("editCollectionBtn").disabled = true;
    $("#collection_public_state").prop('disabled', false);
    var name = $("#collectionName").text();
    $("#collectionName").attr("contenteditable", "true");
    $("#collectionName").css("background-color", "white");
    var description = $("#collectionDescription").text();
    $("#collectionDescription").attr("contenteditable", "true");
    $("#collectionDescription").css("background-color", "white");


}

/**
 * Set elements of collection.html from edit mode back to normal mode
 */
function setCollectionUneditable() {
    console.log("setCollectionUneditable()");
    $("#collectionName").removeAttr('contenteditable style');
    $("#collectionDescription").removeAttr('contenteditable style');
    $("#collection_public_state").prop('disabled', true);
    document.getElementById("saveCollectionBtn").disabled = true;
    document.getElementById("editCollectionBtn").disabled = false;
}

/**
 * Get parameter by given name from current URL
 * @param paramName name of parameter (e.g.: ?id=)
 * @returns {string}
 */
function getParameterFromUrlByName(paramName) {
    var urlParams = new URLSearchParams(window.location.search);
    var param = urlParams.get(paramName);
    console.log("getParameterFromUrlByName(" + paramName + "): " + param);
    return param;
}