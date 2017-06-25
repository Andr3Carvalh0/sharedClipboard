package pt.andre.projecto.Controllers.URIs;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import sun.misc.Request;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

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
    public ResponseEntity createAccount(@RequestParam String account, @RequestParam String password) {
        logger.info(TAG + "createAccount method");

        final DatabaseResponse resp = service.createAccount(account, password);

        logger.info(TAG + "CreateAccount: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Authenticates the user.If the @account and @password, match an account we return the user token.
    * @param account: the user email
    * @param password: the user password
    * */
    @Override
    @RequestMapping(value = "/api/account", params = {"account", "password"}, method = RequestMethod.GET)
    public ResponseEntity authenticate(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        logger.info(TAG + "Authenticate method");
        final DatabaseResponse resp = service.authenticate(account, password);

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
    @PostMapping("/api/pushMIME")
    public ResponseEntity push(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "token") Long token, @RequestParam(value = "deviceIdentifier") String deviceIdentifier) {
        DatabaseResponse resp;

        logger.info(TAG + "Push MIME method");

        resp = service.push(file, token);

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
    public ResponseEntity push(@RequestParam long token, @RequestParam String data, @RequestParam(value = "deviceIdentifier") String deviceIdentifier) {
        logger.info(TAG + "push method");
        final DatabaseResponse resp = service.push(token, data);

        logger.info(TAG + "Push: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).build();
    }

    /*
    * Returns the user data that is stored on the database
    * @param token: the token used to authenticate a user.
    * */
    @Override
    @RequestMapping(value = "/api/pull", params = {"account", "deviceIdentifier"}, method = RequestMethod.GET)
    public ResponseEntity pull(@RequestParam(value = "account") long token,  @RequestParam(value = "deviceIdentifier") String deviceIdentifier) {

        logger.info(TAG + "pull method");

        final DatabaseResponse resp = service.pull(token);

        logger.info(TAG + "Pull: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Associate a mobile Device ID(iOS/Android), so we can use the Firebase Service to push new content to that device
    * @param account: the user account token
    * @param deviceID: the device id, given by the Firebase API
    * */
    private ResponseEntity registerMobileDevice(long account, String deviceID) {
        logger.info(TAG + "RegisterDevice method");
        final DatabaseResponse resp = service.registerMobileDevice(account, deviceID);

        logger.info(TAG + "resgisterDevice: response will have the following code:" + resp.getResponseCode());
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    /*
    * Associate device to the account so we can remove it later
    * @param account: the user account token
    * @param deviceIdentifier: the device id
    * @param deviceType: true: mobile, false:desktop
    * */
    @Override
    @RequestMapping(value = "/api/registerDevice", method = RequestMethod.PUT)
    public ResponseEntity associateDeviceWithAccount(@RequestParam long account, @RequestParam String deviceIdentifier, @RequestParam boolean deviceType) {


        if(deviceType)
            registerMobileDevice(account, deviceIdentifier);

        throw new NotImplementedException();
    }
}
