$( document ).ready(function() {

    // var titles = {"collections":[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}]};

    //var titles =[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}];
    getAllCollections();


});




function getAllCollections() {
    $.ajax({
        url: "/collections",
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            renderCollections(data);
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
            "name": $('#formCreateColletionName').val(),
            "description": $('#formCreateColletionDescription').val()
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

}

function appendToBody(index, collection) {
     
    var template = Handlebars.compile($('#collection-template').html());

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
    $('#formCreateColletionName').val("");
    $('#formCreateColletionDescription').val("");
}

$('#create_new_collection_submit').click(function(event) {
    createCollection()
});