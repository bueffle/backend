function signup(data) {

    $.ajax({
        url: "/user",
        type: 'POST',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),
        success: function(data) {
            $('#signupModal').modal('hide');
            console.log(data);
            window.open(window.location.origin+"/login","_self");
        },
        error:function(data){
            console.log("error");
            console.log(data);
        },
        complete: function(data){
            $('#signupModal').modal('hide');
            console.log(data);
            createCookie("bueffle-user", data.username, 1);
            window.open(window.location.origin+"/login","_self");
            checkLoggedin();
        }
    });
}

function login(username, password){
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
            xhr.setRequestHeader('Authorization', 'Basic ' + btoa(username+':'+password));
        },
        success: function(resp){
            console.log("success");
        },
        error: function(resp){
            console.log("error");
        },
        complete: function(resp){}
    });

}

$('#signup_accept').click(function(event){
    var email=$('#signupEmail').val();
    var password1=$('#signupPassword1').val();
    var password2=$('#signupPassword2').val();
    var filter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(filter.test(email)) {
        if(password1==password2) {
            signup({
                "username": email,
                "password": password1,
                "passwordConfirm": password2
            });
        } else {
            $('#signupPassword1').css("background-color","#eb4034");
            $('#signupPassword2').css("background-color","#eb4034");
        }
    } else {
        $('#signupEmail').css("background-color","#eb4034");
    }

});




function checkLoggedin() {
    $.ajax({
        url: "/user",
        type: 'GET',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        success: function(resp){
            console.log(resp);
            if (resp.username && resp.username != "") {
                showUsername(resp.username);
            } else {
                var login_required = ["/mycollections.html","/profile.html"];
                if (login_required.includes(window.location.pathname)) {
                    window.location="/login";
                }
            }
        },
        error: function(resp){
            console.log(resp);
        },
        complete: function(resp){}
    });
}

function showUsername(user_name) {
    $('#login_button_group').empty();
    var userProfile = document.createElement('a');
    var userLogout = document.createElement('a');
    userProfile.setAttribute('class', 'badge badge-secondary float-right');
    userLogout.setAttribute('class', 'badge badge-dark float-right');
    userProfile.setAttribute('id', 'user_profile_button');
    userLogout.setAttribute('id', 'user_logout_button');
    userProfile.setAttribute('href', '/myprofile.html'); 
    userLogout.setAttribute('href', '#'); 
    userLogout.innerText = "Abmelden";  
    userProfile.innerText = user_name;  
    $('#login_button_group').append(userProfile);
    $('#login_button_group').append(document.createElement('br'));
    $('#login_button_group').append(userLogout);
    $('#user_logout_button').click(function(){
        logout();
    });
    $('#user_profile_button').click(function(){
        console.log("show user profile");
    });
}

function logout() {
    window.location="/logout";
}

$( document ).ready(function() {
    checkLoggedin();
});
