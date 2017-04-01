const CREATE_ACCOUNT_URL = '/api/account'

function createAccount(){
    let email = document.getElementById("emailTextView").value
    let password = document.getElementById("passwordTextView").value

    let tmp = hasAuthenticationErrors(email, password)

    if(!tmp.areFieldsValid){
        displayErrors(tmp.isEmailValid, tmp.isPasswordValid)
        return;
    }

    let data = 'account=' + email + '&password=' + password
    ajaxRequest('PUT', CREATE_ACCOUNT_URL, data)
        .then((response) => {console.log(response)})
        .catch((error) => {console.log(error)})
}
