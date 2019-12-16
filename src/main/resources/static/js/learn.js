/**
 * On document ready function
 */
$( document ).ready(function() {
    var current_learn = JSON.parse(readCookie("learnrun"));
    if(current_learn) {
        getNextCard(current_learn["learnrun"]);
    } else {
        // go back to collections
        alert("current_learn not defined")
        window.location = "/collections.html";
    }

});



function getNextCard(leanrun_id) {
    $.ajax({
        url: "/learn/"+leanrun_id+"/next",
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
    var current_learn = JSON.parse(readCookie("learnrun"));
    var settings = {
        url: "/learn/"+current_learn["learnrun"],
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
        var current_learn = JSON.parse(readCookie("learnrun"));
        console.log(response);
        getNextCard(current_learn["learnrun"]);
      });
}



