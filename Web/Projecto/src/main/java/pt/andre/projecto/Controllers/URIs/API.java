package pt.andre.projecto.Controllers.URIs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Controllers.Interfaces.IAPI;
import pt.andre.projecto.Output.Interfaces.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;

import java.io.IOException;
import java.util.Base64;

@RestController
@AutoConfigureBefore
/*
* Controller that handles every request to the API.
* Implementation of the IAPI Class.The methods functions are described in the IAPI interface
* */
public class API implements IAPI {

    @Autowired
    IAPIService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: API ";

    /*
    * Creates a new user account.
    * @param account: A email that identifies the user.
    * @param password: The password for the account.
    * */
    @Override
    @RequestMapping(value = "/api/account", method = RequestMethod.PUT)
    public ResponseEntity createAccount(@RequestHeader("Authorization") String token) {
        logger.info(TAG + "createAccount method");

        String sub = service.handleAuthentication(token);

        final DatabaseResponse resp = service.createAccount(Base64.getEncoder().encodeToString(sub.getBytes()));

        logger.info(TAG + "CreateAccount: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Authenticates the user.If the @account and @password, match an account we return the user token.
    * @param account: the user email
    * @param password: the user password
    * */
    @Override
    @RequestMapping(value = "/api/account", method = RequestMethod.POST)
    public ResponseEntity authenticate(@RequestHeader("Authorization") String token) {
        String sub;
        try {
            sub = service.handleAuthentication(token);
        }catch (IllegalArgumentException e){
            sub = null;
        }
        logger.info(TAG + "Authenticate method");

        final DatabaseResponse resp = service.authenticate(Base64.getEncoder().encodeToString(sub.getBytes()));

        logger.info(TAG + "Authenticate: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }


    /*
    * Handle a MIME file push.
    * @param file: the file.This can be an image, a document, etc...
    * @param token: the token used to authenticate a user.
    * @param deviceIdentifier: String to identify the device that made the request
    * */
    @Override
    @PostMapping("/api/push")
    public ResponseEntity push(@RequestParam(value = "file") MultipartFile file, @RequestHeader("Authorization") String sub, @RequestParam String device) {
        logger.info(TAG + "Push MIME method");

        try {
            return push(sub, file.getBytes(), file.getOriginalFilename(), device);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity push(String token, byte[] file, String filename, String device) {
        DatabaseResponse resp = service.push(token, file, filename, device);
        logger.info(TAG + "pushMIME: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Handle a textual push.
    * @param token: the token used to authenticate a user.
    * @param data: the data that gets pushed.
    * @param deviceIdentifier: String to identify the device that made the request
    * */
    @Override
    @RequestMapping(value = "/api/push", method = RequestMethod.PUT)
    public ResponseEntity push(@RequestHeader("Authorization") String sub, @RequestParam String data, @RequestParam String device) {
        logger.info(TAG + "push method");

        final DatabaseResponse resp = service.push(sub, data, device);

        logger.info(TAG + "Push: response will have the following code:" + resp.getResponseCode());

        return ResponseEntity.status(resp.getResponseCode()).body((String)resp.getResponseMessage());
    }

    /*
    * Returns the user data that is stored on the database
    * @param token: the token used to authenticate a user.
    * */
    @Override
    @RequestMapping(value = "/api/pull", method = RequestMethod.GET)
    public ResponseEntity pull(@RequestHeader("Authorization") String sub) {
        logger.info(TAG + "pull method");

        final DatabaseResponse resp = service.pull(sub);

        logger.info(TAG + "Pull: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Associate device to the account so we can remove it later
    * @param account: the user account token
    * @param deviceIdentifier: the device id
    * @param isMobile: true: mobile, false:desktop
    * */
    @Override
    @RequestMapping(value = "/api/registerDevice", method = RequestMethod.PUT)
    public ResponseEntity associateDeviceWithAccount(@RequestHeader("Authorization") String sub, @RequestParam String deviceIdentifier, @RequestParam boolean useSockets, @RequestParam String deviceName) {
        if(!useSockets) {
            logger.info(TAG, "AssociateDevice Called! Its a mobile device!");
            return registerMobileDevice(sub, deviceIdentifier, deviceName);
        }

        logger.info(TAG, "AssociateDevice Called! Its a desktop device!");
        return registerDesktopDevice(sub, deviceIdentifier,  deviceName);
    }

    @Override
    @RequestMapping(value = "/api/MIME/{encryptedSUB}/{file:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> serveMIME(@PathVariable("encryptedSUB") String encryptedSUB, @RequestHeader("Authorization") String sub, @PathVariable("file") String file) {
        logger.info(TAG + "ServeMIME called!");

        final DatabaseResponse resp = service.handleMIMERequest(encryptedSUB, sub, file);

        logger.info(TAG + "ServeMIME: response will have the following code:" + resp.getResponseCode());

        return ResponseEntity.status(resp.getResponseCode()).body((byte[])resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/socket", method = RequestMethod.GET)
    public ResponseEntity getWebSocketPort() {
        DatabaseResponse resp = service.getSocket();
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }


    private ResponseEntity registerDesktopDevice(String sub, String deviceIdentifier, String deviceName) {
        logger.info(TAG + "RegisterDevice method");
        final DatabaseResponse resp = service.registerDesktopDevice(sub, deviceIdentifier, deviceName);

        logger.info(TAG + "registerDevice: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Associate a mobile DeviceWrapper ID(iOS/Android), so we can use the Firebase Service to push new content to that device
    * @param account: the user account token
    * @param deviceID: the device id, given by the Firebase API
    * */
    private ResponseEntity registerMobileDevice(String sub, String deviceID, String deviceName) {
        logger.info(TAG + "RegisterDevice method");
        final DatabaseResponse resp = service.registerMobileDevice(sub, deviceID, deviceName);

        logger.info(TAG + "registerDevice: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

}
