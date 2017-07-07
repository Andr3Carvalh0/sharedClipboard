
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

function displayMessage(message, modal) {
    let messageField = document.getElementById("errorLabel" + modal)
    messageField.innerHTML = '<li>' + message + '</li>'
}
