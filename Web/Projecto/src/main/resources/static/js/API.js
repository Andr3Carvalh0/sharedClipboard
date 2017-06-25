const CREATE_ACCOUNT_URL = '/api/account'

//@todo: change the ajax Request to fetch
function createAccount() {
    if(commonAuthentication("")) {
        let data = 'account=' + email + '&password=' + password
        ajaxRequest('PUT', CREATE_ACCOUNT_URL, data)
            .then((response) => {
                //This is a hack.Since this stupid version of the UIKIT doesn't support closing modal via javascript.
                //Simulate a button click
                $("#modal_Cancel_Button").click()

                //Show a notification alerting the user that the creation of the account was a success
                UIkit.notification("Success!", {pos: 'top-right', status: 'success'});

            })
            .catch((response) => {
                displayMessage(response)

            })
    }
}

function loginAccount() {

    if(commonAuthentication("_preferences")){
        let data = 'account=' + email + '&password=' + password
        ajaxRequest('PUT', CREATE_ACCOUNT_URL, data)
            .then((response) => {

                //Show a notification alerting the user that the creation of the account was a success
                UIkit.notification("Success!", {pos: 'top-right', status: 'success'});

            })
            .catch((response) => {

                displayMessage(response)

            })
    }
}

function commonAuthentication(modal) {
    let email = document.getElementById("emailTextView" + modal).value
    let password = document.getElementById("passwordTextView" + modal).value

    let tmp = hasAuthenticationErrors(email, password)

    if (!tmp.areFieldsValid) {
        displayErrors(tmp.isEmailValid, tmp.isPasswordValid, modal)
        return false;
    }

    return true;
}
