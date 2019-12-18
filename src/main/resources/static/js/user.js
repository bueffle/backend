/**
 * Collection of header related function
 * 
 * User.js is loaded as part of the header/nav template this script provides the function related to the signup and search
 *
 * @link   https://github.com/bueffle/backend/blob/master/src/main/resources/static/js/collections.js
 * @author AuthorName.
 */


/**
 * Signup function to create a user with the given params
 * 
 * @param {Object} data {username:String,password:String,passwordRepeat:String}
 */
function signup(data) {

    $.ajax({
        url: "/user",
        type: 'POST',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),
        success: function (data) {
            $('#signupModal').modal('hide');
            console.log(data);
            window.open(window.location.origin + "/login", "_self");
        },
        error: function (data) {
            console.log("error");
            console.log(data);
        },
        complete: function (data) {
            $('#signupModal').modal('hide');
            console.log(data);
            createCookie("bueffle-user", data.username, 1);
            window.open(window.location.origin + "/login", "_self");
            checkLoggedin();
        }
    });
}

/**
 * login calls the user backend call which in return, sets the JSESSION cookie which is used for authentication 
 * This function is currently unused 
 * 
 * @param {Sting} username 
 * @param {Sting} password 
 */
function login(username, password) {
    $.ajax({
        url: "/user",
        type: 'GET',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        xhrFields: {
            withCredentials: true
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Basic ' + btoa(username + ':' + password));
        },
        success: function (resp) {
            console.log("success");
        },
        error: function (resp) {
            console.log("error");
        },
        complete: function (resp) {}
    });

}

/**
 * On-Click event for Signup with VERY simple input validation
 */
$('#signup_accept').click(function (event) {
    var email = $('#signupEmail').val();
    var password1 = $('#signupPassword1').val();
    var password2 = $('#signupPassword2').val();
    var filter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (filter.test(email)) {
        if (password1 == password2) {
            signup({
                "username": email,
                "password": password1,
                "passwordConfirm": password2
            });
        } else {
            $('#signupPassword1').css("background-color", "#eb4034");
            $('#signupPassword2').css("background-color", "#eb4034");
        }
    } else {
        $('#signupEmail').css("background-color", "#eb4034");
    }

});



/**
 * Checks if a user is logged in or not. Depending on result the login and signup button are replaced with username and logout
 * If the user is not logged in and on a page which requires login, redirect to login page. 
 */
function checkLoggedin() {
    $.ajax({
        url: "/user",
        type: 'GET',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        success: function (resp) {
            console.log(resp);
            if (resp.username && resp.username != "") {
                showUsername(resp.username);
            } else {
                var login_required = ["/mycollections.html", "/profile.html"];
                if (login_required.includes(window.location.pathname)) {
                    window.location = "/login";
                }
            }
        },
        error: function (resp) {
            console.log(resp);
        },
        complete: function (resp) {}
    });
}

/**
 * Creates and appends the DOM elemnts required for the header when a user is logged in
 * @param {String} user_name 
 */
function showUsername(user_name) {
    $('#login_button_group').empty();
    var userProfile = document.createElement('a');
    var userLogout = document.createElement('a');
    var myCollections = document.createElement('a');
    myCollections.setAttribute('class', 'btn btn-secondary');
    myCollections.setAttribute('role', 'button');
    myCollections.setAttribute('href', '/mycollections.html');
    userProfile.setAttribute('class', 'badge badge-secondary float-right');
    userLogout.setAttribute('class', 'badge badge-dark float-right');
    userProfile.setAttribute('id', 'user_profile_button');
    userLogout.setAttribute('id', 'user_logout_button');
    userProfile.setAttribute('href', '/myprofile.html');
    userLogout.setAttribute('href', '#');
    userLogout.innerText = "Abmelden";
    userProfile.innerText = user_name;
    myCollections.innerText = "Meine Sammlungen"
    $(myCollections).insertBefore("#login_button_group");
    $('#login_button_group').append(userProfile);
    $('#login_button_group').append(document.createElement('br'));
    $('#login_button_group').append(userLogout);
    $('#user_logout_button').click(function () {
        logout();
    });
    $('#user_profile_button').click(function () {
        console.log("show user profile");
    });
}

/**
 * Logs the user out be redirecting to the logout page which removes the cookie
 */
function logout() {
    window.location = "/logout";
}

/**
 * When the document is finish loading check if the user is logged in
 */
$(document).ready(function () {
    checkLoggedin();
});

/**
 * On keypress event for search. Only executes if the search length is larger then 1 character
 */
$('#collection_search').on('keypress', function (e) {
    if (e.which == 13 && $('#collection_search').val().length > 1) {
        window.location.href = window.location.origin + "/collections.html?search=" + $('#collection_search').val();
    }
});