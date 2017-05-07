const CREATE_ACCOUNT_URL = '/api/account'

function createAccount() {
    let email = document.getElementById("emailTextView").value
    let password = document.getElementById("passwordTextView").value

    let tmp = hasAuthenticationErrors(email, password)

    if (!tmp.areFieldsValid) {
        displayErrors(tmp.isEmailValid, tmp.isPasswordValid)
        return;
    }

    let data = 'account=' + email + '&password=' + password
    ajaxRequest('PUT', CREATE_ACCOUNT_URL, data)
        .then((response) => {

            //This is a hack.Since the stupid UIKIT doesnt support closing modal via javascript.Simulate a button click
            $("#modal_Cancel_Button").click()

            //Show a notification alerting the user that the creation of the account was a success
            UIkit.notification("Success!", {pos: 'top-right', status: 'success'});

        })
        .catch((response) => {
            displayMessage(response)

        })
}
