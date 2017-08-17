package pt.andre.projecto.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Controllers.URIs.FirebaseService;
import pt.andre.projecto.Controllers.URIs.WebSocketService;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Model.Multimedia.IMultimediaHandler;
import pt.andre.projecto.Model.Utils.JSONFormatter;
import pt.andre.projecto.Service.Interfaces.IAPIService;

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
    public DatabaseResponse push(MultipartFile file, String sub) {

        String result = storeFile(sub, file);

        if(result == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

        return this.push(sub, result, true);
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
    private String storeFile(String sub, MultipartFile file){
        return multimediaHandler.store(sub, file);
    }

    /*
    * Handles an request push.
    * It stores the data in the database, and starts the process of notifying the user mobile devices
    *
    * @param token: the user account
    * @param data: the user data
    * @param isMIME: indicates whether the data is an URL to the real content or if its the real content
    * */
    private DatabaseResponse push(String sub, String data, boolean isMIME) {
        DatabaseResponse push = database.push(sub, data, isMIME);

        logger.info(TAG + "Sharing with devices");


        final String[] mobileDevices = database.getMobileDevices(sub)
                .stream().map(DeviceWrapper::getId)
                .toArray(String[]::new);

        final String[] desktopDevices = database.getDesktopDevices(sub)
                .stream()
                .map(DeviceWrapper::getId)
                .toArray(String[]::new);

        firebaseService.notify(sub, JSONFormatter.formatToJSON(data, isMIME), mobileDevices);
        webSocketService.notify(sub, JSONFormatter.formatToJSON(data, isMIME), desktopDevices);

        return push;
    }
}
