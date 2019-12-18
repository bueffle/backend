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
                $.ajax({url: "/learn/"+leanrun_id,
                type: 'GET',
                dataType: 'json',
                complete: function(data) {
                    var richtig = data.responseJSON.cardInLearningRuns.length;
                    var falsch = 0;
                    for(var i in data.responseJSON.cardInLearningRuns ) {
                        falsch += data.responseJSON.cardInLearningRuns[i]["shownCounter"] - 1;
                    }
                    var template  = Handlebars.compile($('#lean-finished-modal-template').html());
                    var html=template({richtig:richtig,falsch:falsch});
                    $('#finished_modal').html(html);

                    $('#leaningEndModal').modal();
                }
            });
                
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



