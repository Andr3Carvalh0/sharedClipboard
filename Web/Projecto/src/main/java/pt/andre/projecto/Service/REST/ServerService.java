package pt.andre.projecto.Service.REST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import pt.andre.projecto.Controllers.URIs.FirebaseService;
import pt.andre.projecto.Controllers.URIs.WebSocketService;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.Interfaces.IDatabase;
import pt.andre.projecto.Model.Utils.DefaultHashMap;
import pt.andre.projecto.Model.Utils.Device;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;
import pt.andre.projecto.Output.MensageFormater;
import pt.andre.projecto.Service.Interfaces.IServerService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
* Handles the representation of the website main page
* */
public class ServerService extends ParentService implements IServerService{
    private String CLIENT_SECRET_ID;

    private DefaultHashMap<String, String> FILES_TO_DOWNLOAD;
    @Autowired
    private IDatabase database;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSocketService webSocketService;

    private final Resource resource = new ClassPathResource("/application.properties");

    public ServerService(){
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            this.FILES_TO_DOWNLOAD = new DefaultHashMap<>("");
            FILES_TO_DOWNLOAD.put("Windows", props.getProperty("WindowsApp"));
            FILES_TO_DOWNLOAD.put("Android", props.getProperty("AndroidApp"));

            this.CLIENT_SECRET_ID = props.getProperty("googleID");

            logger.info(TAG, "Loaded successfully Google ID from props file.");
        } catch (IOException e) {
            logger.error(TAG, "Cannot load Google ID from properties file.");
        }
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: ServerService ";


    /*
    * Responsable for returning the website UI
    *
    * @param model: object used to store the key-value used to populate our handlebar file.
    * @param userAgent: string that identifies the user device.
    * @param deviceIdentifier: Object that knows how to transform an userAgent into an object that we know how to work with.
    *
    * @returns a handlebar file that was populated with information based on the user's device
    * */
    @Override
    public String handleRootRequest(Map<String, Object> model, String userAgent, DeviceIdentifier deviceIdentifier) {
        Device currentDevice = deviceIdentifier.getDeviceInformation(userAgent);

        logger.info(TAG + "OS Detected: " + currentDevice.getOS() + ", based on:" + userAgent);

        //Application name(The key is Link_2_The_Past, because why not?Its one of my favourite games...)
        model.put("Link_2_The_Past", "Shared Clipboard");

        //Information related to the device that made the request
        model.put("OS", currentDevice.getOS());
        model.put("OS_Name", currentDevice.getOS_Friendly_Name());
        model.put("isSupported", currentDevice.isSupported());
        model.put("GoogleID", CLIENT_SECRET_ID);
        model.put("Application", FILES_TO_DOWNLOAD.get(currentDevice.getOS_Friendly_Name()));

        return "index";
    }

    @Override
    public String getDevices(Map<String, Object> model, String sub) {
        if(sub == null)
            return "empty";

        return getDevicesWithSub(model, sub);
    }

    @Override
    public String deleteDevice(Map<String, Object> model, String sub, String deviceIdentifier, boolean isMobile) {
        final boolean result = database.removeDevice(sub, deviceIdentifier, isMobile);

        if(!result)
            return "error";

        notifyRemovedDevice(sub, deviceIdentifier, isMobile);

        return getDevicesWithSub(model, sub);
    }

    public void notifyRemovedDevice(String sub, String deviceID, boolean isMobile){
        if(!isMobile) {
            webSocketService.notify(sub, MensageFormater.expel(), deviceID);
            return;
        }
        firebaseService.notify(sub, MensageFormater.expel(), deviceID);
    }

    private String getDevicesWithSub(Map<String, Object> model, String sub) {
        List<DeviceWrapper> devices = database.getDesktopDevices(sub);
        devices.addAll(database.getMobileDevices(sub));

        if(devices.size() == 0)
            return "empty";

        model.put("devices", devices);
        return "devicesList";
    }
}
