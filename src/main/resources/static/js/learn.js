/**
 * On document ready function
 */
$( document ).ready(function() {
    var search = parseSearch();
    if(search && search['collection_id']) {
        getNextCard(search['collection_id']);
    } else {
        // go back to collections
    }

});


function getNextCard(collection_id) {
    $.ajax({
        url: "/learn/"+collection_id+"/next",
        type: 'GET',
        dataType: 'json',
        complete: function(data) {
            console.log(data);

            getTemplateAjax("templates/card.handlebars","learn_card_container",)
        }
    });
};

var demo= {
    "id": 20,
    "question": "der Computer",
    "answer": "la computadora",
    "collections": [],
    "owner": null,
    "public": true
};