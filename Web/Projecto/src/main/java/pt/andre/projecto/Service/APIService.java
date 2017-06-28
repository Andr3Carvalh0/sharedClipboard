package pt.andre.projecto.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Controllers.URIs.FirebaseServer;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import pt.andre.projecto.WebSockets.WebSocket;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/*
* Service that handles every action to our API URLs
* */
public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;

    @Autowired
    WebSocket webSocket;

    @Autowired
    private FirebaseServer firebaseServer;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: APIService ";

    /*
    * Handles a textual(only string) push request
    *
    * @param token: the user account
    * @param data: the user textual data
    * */
    @Override
    public DatabaseResponse push(long token, String data) {

        return this.push(token, data, false);
    }


    /*
    * Handles a MIME push request
    *
    * @param token: the user account
    * @param data: the user textual data
    * */
    @Override
    public DatabaseResponse push(MultipartFile file, long token) {

        String result = storeFile(token, file);

        if(result == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

        return this.push(token, result, true);
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
    public DatabaseResponse registerMobileDevice(long token, String firebaseID, String deviceName) {
        return database.registerMobileDevice(token, firebaseID, deviceName);
    }

    @Override
    public DatabaseResponse registerDesktopDevice(long token, String deviceID, String deviceName) {
        return database.registerDesktopDevice(token, deviceID, deviceName);
    }

    /*
    * Handles a pull request.
    * @param token: the user account token
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse pull(long token) {
        return database.pull(token);
    }

    /*
    * Handles the creation of an account
    * @param account: the user email
    * @param password: the user password
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse createAccount(String account, String password) {
        return database.createAccount(account, password);
    }

    /*
    * Handles an authentication request
    * @param account: the user email
    * @param password: the user password
    *
    * @return DatabaseResponse: Object that contains the server HTTP code, and a Message.
    * */
    @Override
    public DatabaseResponse authenticate(String account, String password) {
        return database.authenticate(account, password);
    }

    /*
    * Used when the user pushed a MIME file to the server.
    * @param token: the user account
    * @param file: the user file
    * */
    private String storeFile(long token, MultipartFile file){
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                logger.info(TAG + "Attempting to create directories");
                // Create the directory structure if it isn't already created.
                File outFile = new File("build/resources/main/static/content/" + token + "/");
                outFile.mkdirs();


                logger.info(TAG + "Directories created!");
                // Writes the file
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("build/resources/main/static/content/" + token + "/" + file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                logger.info(TAG + "File created!");
                return System.getenv("SERVER") + "/content/" + token + "/" + file.getOriginalFilename();
            } catch (Exception e) {
                logger.error(TAG + e.getMessage());
                return null;
            }
        }
        logger.error(TAG + "file is empty");
        return null;
    }

    /*
    * Handles an request push.
    * It stores the data in the database, and starts the process of notifying the user mobile devices
    *
    * @param token: the user account
    * @param data: the user data
    * @param isMIME: indicates whether the data is an URL to the real content or if its the real content
    * */
    private DatabaseResponse push(long token, String data, boolean isMIME) {
        DatabaseResponse push = database.push(token, data, isMIME);

        logger.info(TAG + "Sharing with devices");

        String[] devices = database.getDesktopDevices(token)
                .stream()
                .map(DeviceWrapper::getId)
                .collect(Collectors.toList())
                .toArray(new String[0]);


        firebaseServer.notify(data, isMIME, devices);

        sendMessageToDesktopDevices(token, data, isMIME);

        return push;
    }

    private void sendMessageToDesktopDevices(long token, String data, boolean isMIME) {
        List<DeviceWrapper> devices = database.getDesktopDevices(token);
        String message = "{" + "\n\rcontent: " + data + ", " + "\n\risMIME: " + isMIME + "}";

        for (DeviceWrapper device : devices) {

            try {
                webSocket.getHandler().sendMessage(device.getId(), message);
            } catch (InterruptedException e) {
                logger.info("Cannot send message to: " + device);
            }
        }
    }


}
