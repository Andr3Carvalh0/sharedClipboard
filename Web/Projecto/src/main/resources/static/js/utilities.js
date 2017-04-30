let resetMap = {
    'email' : () => { document.getElementById("emailTextView").className = 'uk-input' },
    'password' : () => { document.getElementById("passwordTextView").className = 'uk-input' }


}

function ajaxRequest(method, path, data) {
    const promise = new Promise((resolve, reject) => {
        const xmlhttp = new XMLHttpRequest()
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
                if (xmlhttp.status == 200) {
                    resolve(xmlhttp.responseText)
                }
                else {
                    reject(new Error(xmlhttp.responseText))
                }
            }
        }
        xmlhttp.open(method, path, true)
        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
        xmlhttp.send(data)
    })
    return promise
}

function hasAuthenticationErrors(email, password) {
    let validEmail = verifyEmail(email)
    let validPassword = verifyPassword(password)

    return {
        areFieldsValid: validEmail && validPassword,
        isEmailValid : validEmail,
        isPasswordValid : validPassword
    }
}

//Taken from chromium/Webkit source
function verifyEmail(email) {
    let configuration = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return configuration.test(email);
}

function verifyPassword(password) {
    return password.length >= 6
}

function displayErrors(emailValid, passwordValid) {
    let emailField = document.getElementById("emailTextView")
    let passwordField = document.getElementById("passwordTextView")
    let messageField = document.getElementById("errorLabel")
    let message = " "


    if(!emailValid){
        emailField.className += ' uk-form-danger'
        message += '<li>' + " Email address was not valid." + '</li>'
    }

    if(!passwordValid){
        passwordField.className += ' uk-form-danger'
        message += '<li>' + " Password was not valid. It must contain at least 6 letters." + '</li>'
    }

    messageField.innerHTML = message
}

function displayMessage(message) {
    let messageField = document.getElementById("errorLabel")
    messageField.innerHTML = '<li>' + message + '</li>'
}

function resetErrorClues(type, resetErrorTips) {
    resetMap[type]()

    if(resetErrorTips){
        document.getElementById("emailTextView").value = ""
        document.getElementById("passwordTextView").value = ""
        document.getElementById("errorLabel").innerHTML = ""
    }
}