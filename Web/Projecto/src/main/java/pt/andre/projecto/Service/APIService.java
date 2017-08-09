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
import java.io.*;


/*
* Service that handles every action to our API URLs
* */
public class APIService extends ParentService implements IAPIService{

    @Autowired
    private IDatabase database;

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
        if(sub == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

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
        if(sub == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

        return database.authenticate(sub);
    }

    /*
    * Used when the user pushed a MIME file to the server.
    * @param token: the user account
    * @param file: the user file
    * */
    private String storeFile(String sub, MultipartFile file){
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                logger.info(TAG + "Attempting to create directories");
                // Create the directory structure if it isn't already created.
                File outFile = new File("build/resources/main/static/content/" + sub + "/");
                outFile.mkdirs();


                logger.info(TAG + "Directories created!");
                // Writes the file
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("build/resources/main/static/content/" + sub + "/" + file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                logger.info(TAG + "File created!");
                return System.getenv("SERVER") + "/content/" + sub + "/" + file.getOriginalFilename();
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


        firebaseServer.notify(data, isMIME, mobileDevices);

        sendMessageToDesktopDevices(data, isMIME, desktopDevices);

        return push;
    }

    private void sendMessageToDesktopDevices(String data, boolean isMIME, String...devices) {
        String message = "{" + "\n\rcontent: " + data + ", " + "\n\risMIME: " + isMIME + "}";

        for (String device : devices) {

            try {
               // webSocket.getHandler().sendMessage(device, message);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.info("Cannot send message to: " + device);
            }
        }
    }


}
