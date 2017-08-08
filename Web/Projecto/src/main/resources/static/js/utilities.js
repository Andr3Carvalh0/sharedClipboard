function authenticate(auth) {
    //Fix.By default the button will disappear.Override that.
    $('#signinButton').removeAttr('hidden');

    //Hack to open modal
    $('#hackyMChacky').click();

    // Send the code to the server
    $.ajax({
        type: 'POST',
        url: '/api/account',
        // Always include an `X-Requested-With` header in every AJAX request,
        // to protect against CSRF attacks.
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'Authorization': auth
        },
        contentType: 'application/x-www-form-urlencoded',
        success: function(result) {
            getUserDevices(auth)
        },
        statusCode: {
            400: function() {
                //Close the current modal. And open the account creation modal
                $('#hackyMChacky').click();
                createAccount(auth)
            },
            500: function () {
                displayMessage("Error on login. Try later.", false)
            }
        },
        processData: false
    });
}

function getUserDevices() {
    console.log("TODO")

}

function createAccount(auth) {
    //Shows modal
    $('#createAccount').click();

    $('#createAccountConfirmation').click(function() {
        $.ajax({
            type: 'PUT',
            url: '/api/account',
            headers: {
                'Authorization': auth
            },
            contentType: 'application/x-www-form-urlencoded',
            success: function(result) {
                displayMessage("Account created. Enjoy!", true)
                $('#createAccount').click();
            },
            error: function(){
                displayMessage("An error occurred. Try again later.", false)
                $('#createAccount').click();
            },
            processData: false
        });
    })


}

function displayMessage(data, success) {
    UIkit.notification({
        message: data,
        status: success ? 'primary' : 'danger',
        pos: 'top-right'
    });
}
