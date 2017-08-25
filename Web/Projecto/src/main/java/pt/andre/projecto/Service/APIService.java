package pt.andre.projecto.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Controllers.URIs.FirebaseService;
import pt.andre.projecto.Controllers.URIs.WebSocketService;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Model.Multimedia.IMultimediaHandler;
import pt.andre.projecto.Model.Utils.MensageFormater;
import pt.andre.projecto.Service.Interfaces.IAPIService;

import java.util.concurrent.Callable;

/*
* Service that handles every action to our API URLs
* */
public class APIService extends ParentService implements IAPIService{

    @Autowired
    private IDatabase database;

    @Autowired
    private IMultimediaHandler multimediaHandler;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSocketService webSocketService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: APIService ";

    /*
    * Handles a textual(only string) push request
    *
    * @param token: the user account
    * @param data: the user textual data
    * */
    @Override
    public DatabaseResponse push(String sub, String data) {
        return this.push(sub, data, false);
    }


    /*
    * Handles a MIME push request
    *
    * @param token: the user account
    * @param data: the user textual data
    * */
    @Override
    public DatabaseResponse push(String sub, byte[] file, String filename) {
        String result = storeFile(sub, file, filename);

        if(result == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

        return this.push(sub, result, true, file, filename);
    }

    /*
    * Handles the registration of mobile device ID.
    * This is used so when can push the information to the device wihtout doing pool on it.
    *
    * @param token: the user account token
    * @param firebaseID: the device Firebase ID
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName) {
        return database.registerMobileDevice(sub, firebaseID, deviceName);
    }

    @Override
    public DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName) {
        return database.registerDesktopDevice(sub, deviceID, deviceName);
    }

    /*
    * Handles a pull request.
    * @param token: the user account token
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse pull(String sub) {
        return database.pull(sub);
    }

    /*
    * Handles the creation of an account
    * @param account: the user email
    * @param password: the user password
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse createAccount(String sub) {
        return database.createAccount(sub);
    }

    /*
    * Handles an authentication request
    * @param account: the user email
    * @param password: the user password
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse authenticate(String sub) {
        return database.authenticate(sub);
    }

    @Override
    public DatabaseResponse handleMIMERequest(String encryptedSUB, String sub, String file) {
        return multimediaHandler.pull(encryptedSUB, sub, file);
    }

    /*
    * Used when the user pushed a MIME file to the server.
    * @param token: the user account
    * @param file: the user file
    * */
    private String storeFile(String sub, byte[] file, String filename){
        return multimediaHandler.store(sub, file, filename);
    }

    private DatabaseResponse push(String sub, String data, boolean isMIME) {
        return pushCommon(sub, data, isMIME,  () -> MensageFormater.updateMessage(data, isMIME));
    }

    private DatabaseResponse push(String sub, String data, boolean isMIME, byte[] file, String filename) {
        return pushCommon(sub, data, isMIME,() -> MensageFormater.updateMessage(file, filename));
    }

    private DatabaseResponse pushCommon(String sub, String data, boolean isMIME, Callable<String> message) {
        DatabaseResponse push = database.push(sub, data, isMIME);

        logger.info(TAG + "Sharing with devices");

        final String[] mobileDevices = database.getMobileDevices(sub)
                .stream().map(DeviceWrapper::getId)
                .toArray(String[]::new);

        final String[] desktopDevices = database.getDesktopDevices(sub)
                .stream()
                .map(DeviceWrapper::getId)
                .toArray(String[]::new);

        String desktop_message;

        try {
            desktop_message = message.call();
        } catch (Exception e) {
            //This should never happen but just to be sure!
            desktop_message = MensageFormater.updateMessage(data, isMIME);
        }

        firebaseService.notify(sub, MensageFormater.updateMessage(data, isMIME), mobileDevices);
        webSocketService.notify(sub, desktop_message, desktopDevices);
        return push;
    }
}
