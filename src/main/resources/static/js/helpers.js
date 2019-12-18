/**
 * Helper Javascript file.
 *
 * A collection of commonly used functions. If a function is found to be useful or reused over multiple files, the function should be moved here. 
 * This file must be loaded before any other custom Javascript files. 
 *
 * @link   https://github.com/bueffle/backend/blob/master/src/main/resources/static/js/helpers.js
 * @author AuthorName.
 */


/**
 * Handlebars.js helper function to create a given sequence of actions 
 * e.g. for every 3 inner objects write an outer object 
 * 
 * @param {Int} every
 * @param {Object} context
 * @param {Object} options
 */
Handlebars.registerHelper("nth", function (every, context, options) {
    console.log("TEST");
    var out = "",
        subcontext = [],
        i;
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
 * Creates a cookie for the given values
 * The value can also be a json string to facilitate an object 
 * 
 * @param {String} name 
 * @param {String|Int} value 
 * @param {Int} days 
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
 * Returns the String of the given cookie name or null if not found
 * 
 * @param {String} name 
 * @returns {String|null} 
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
 * Invalidates the cookie for the given name
 * By invalidating the cookie with an expire date -1 day the cookie is effectively deleted 
 * 
 * @param {String} name 
 */
function eraseCookie(name) {
    createCookie(name, "", -1);
}

/**
 * Returns the search string as object {key:value,..}, string or null if undefined
 * 
 * @returns {Object|String|null}
 */
function parseSearch() {
    if (location.search.substring(1) !== "") {
        var search = location.search.substring(1);
        return JSON.parse('{"' + search.replace(/&/g, '","').replace(/=/g, '":"') + '"}', function (key, value) {
            return key === "" ? value : decodeURIComponent(value)
        })
    } else {
        return null;
    }
}


/**
 * Retrieves a Handlebars template from a given path, parses given values if set and inserts it into the given target (DOM id)
 * A callback can be defined as the function is async. 
 * 
 * @param {String} path 
 * @param {String} target 
 * @param {Object} values
 * @param {Function} callback 
 */
function getTemplateAjax(path, target, values = null, callback = null) {
    var source;
    var template;

    $.ajax({
        url: path,
        cache: true,
        success: function (data) {
            source = data;
            template = Handlebars.compile(source);
            if (values) {
                var html = template(values);
                $('#' + target).html(html);
            } else {
                $('#' + target).html(template);
            }

            if (callback) {
                callback();
            }
        }
    });
}


/**
 * On document ready function
 * For loading of global template element's
 */
$(document).ready(function () {
    getTemplateAjax('/templates/header.handlebars', 'header_nav');
});