var collections=jQuery.get('/collections',function(data){console.log(data);})


var source = document.getElementById("entry-template").innerHTML;
var template = Handlebars.compile(source);


for (var collection in collections ){

    var context = { title: collection.name };
    var html = template(context);


    $( "#collections-content" ).append( html );
}