$( document ).ready(function() {

    var titles = {"titles":[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}]};

    //var titles =[{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"},{"title":"tuk,asdfas jnw"}];



    var template = Handlebars.compile($('#entry-template').html());

    var html = template(titles);

    $('#collections-content').append(html);

    testFunction();
});


function testFunction() {
    console.log("test");
}