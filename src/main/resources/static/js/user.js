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
        type: 'POST',
        dataType: 'json',
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(data),
        success: function(resp){},
        error: function(resp){},
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
    if (readCookie("bueffle-user")) {
        showUsername(readCookie("bueffle-user"))
    }
}

function showUsername(user_name) {
    $('#login_button_group').empty();
    var userProfile = document.createElement('a');
    var userLogout = document.createElement('a');
    userProfile.setAttribute('class', '');
    userLogout.setAttribute('class', '');
    userProfile.setAttribute('id', 'user_logout_button');
    userLogout.setAttribute('id', 'user_profile_button');
    userProfile.setAttribute('href', '#'); 
    userLogout.setAttribute('href', '#'); 
    userLogout.innerText = "Abmelden";  
    userProfile.innerText = user_name;  
    $('#login_button_group').append(userProfile);
    $('#login_button_group').append(userLogout);
    $('#user_logout_button').click(function(){
        logout();
    });
    $('#user_profile_button').click(function(){
        console.log("show user profile");
    });
}

function logout() {
    eraseCookie('bueffle-user');
    window.location("/logout");
}
$( document ).ready(function() {
    checkLoggedin();
});
