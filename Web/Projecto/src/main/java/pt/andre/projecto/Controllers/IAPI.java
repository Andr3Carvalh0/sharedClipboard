package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface IAPI {

    /*
    * Handle a textual push.
    * @param token: the token used to authenticate a user.
    * @param data: the data that gets pushed.
    * */
    ResponseEntity push(long token, String data);

    /*
    * Handle a MIME file push.
    * @param file: the file.This can be an image, a document, etc...
    * @param token: the token used to authenticate a user.
    * */
    ResponseEntity push(MultipartFile file, Long token);

    /*
    * Returns the user data that is stored on the database
    * @param token: the token used to authenticate a user.
    * */
    ResponseEntity pull(long token);

    /*
    * Creates a new user account.
    * @param account: A email that identifies the user.
    * @param password: The password for the account.
    * */
    ResponseEntity createAccount(String account, String password);

    /*
    * Authenticates the user.If the @account and @password, match an account we return the user token.
    * @param account: the user email
    * @param password: the user password
    * */
    ResponseEntity authenticate(String account, String password);

    /*
    * Associate a mobile Device ID(iOS/Android), so we can use the Firebase Service to push new content to that device
    * @param account: the user account token
    * @param deviceID: the device id, given by the Firebase API
    * */
    ResponseEntity registerMobileDevice(long account, String deviceID);


}
