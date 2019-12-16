/**
 * On document ready function
 */
$( document ).ready(function() {
    var current_learn = readCookie("learnrun");
    if(current_learn) {
        getNextCard(current_learn);
    } else {
        // go back to collections
        alert("current_learn not defined")
        window.location = "/collections.html";
    }

});

$( window ).unload(function() {
    eraseCookie("learnrun");
  });


function getNextCard(collection_id) {
    $.ajax({
        url: "/learn/"+collection_id+"/next",
        type: 'GET',
        dataType: 'json',
        complete: function(data) {
            $("#learn_card_container").empty();
            if (data.responseJSON) {
                console.info(data.responseJSON);
                getTemplateAjax("templates/card.handlebars","learn_card_container",data.responseJSON)
            } else {
                eraseCookie("learnrun");
                $('#leaningEndModal').modal();
            }
        }
    });
};

function answer(state) {
    var settings = {
        url: "/learn/"+readCookie("learnrun"),
        method: "PUT",
        contentType: 'application/json',
        "data": "{\r\n    \"answeredCorrectly\": \"false\"\r\n}",
        data: JSON.stringify({answeredCorrectly: state}),
        error:function(resp) {
            console.error(resp)
        }
      }
      
    console.log("aswered with" +state);
      $.ajax(settings).done(function (response) {
        console.log(response);
        getNextCard(readCookie("learnrun"));
      });
}



