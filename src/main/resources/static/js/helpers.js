/**
 * 
 */
Handlebars.registerHelper("nth", function(every, context, options){
    console.log("TEST");
    var out = "", subcontext = [], i;
    if (context && context.length > 0) {
        for (i = 0; i < context.length; i++) {
            if (i > 0 && i % every === 0) {
                out += options.fn(subcontext);
                subcontext = [];
            }
            subcontext.push(context[i]);
        }
        out += options.fn(subcontext);
    }
    return out;
});

/**
 * 
 * @param {*} name 
 * @param {*} value 
 * @param {*} days 
 */
function createCookie(name, value, days) {
    var expires;

    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toGMTString();
    } else {
        expires = "";
    }
    document.cookie = encodeURIComponent(name) + "=" + encodeURIComponent(value) + expires + "; path=/";
}

/**
 * 
 * @param {*} name 
 */
function readCookie(name) {
    var nameEQ = encodeURIComponent(name) + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ')
            c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0)
            return decodeURIComponent(c.substring(nameEQ.length, c.length));
    }
    return null;
}

/**
 * 
 * @param {*} name 
 */
function eraseCookie(name) {
    createCookie(name, "", -1);
}

/**
 * 
 */
function parseSearch() {
    if(location.search.substring(1) !== "") {
        var search = location.search.substring(1);
        return JSON.parse('{"' + search.replace(/&/g, '","').replace(/=/g,'":"') + '"}', function(key, value) { return key===""?value:decodeURIComponent(value) })
    } else {
        return null;
    }
}


/**
 * 
 * @param {*} path 
 * @param {*} target 
 * @param {*} values 
 */
function getTemplateAjax(path, target, values=null) {
    var source;
    var template;

    $.ajax({
        url: path, 
        cache: true,
        success: function(data) {
            source    = data;
            template  = Handlebars.compile(source);
            if(values) {
                var html=template(values);
                $('#'+target).html(html);
            } else {
                $('#'+target).html(template);
            }
        }               
    });         
}


/**
 * On document ready function
 * For loading of global template elementes
 */
$( document ).ready(function() {
    getTemplateAjax('templates/header.handlebars','header_nav');
});