package pt.andre.projecto.Service;

import pt.andre.projecto.Model.Utils.Device;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;
import pt.andre.projecto.Service.Interfaces.IServerService;

import java.util.Map;

/*
* Handles the representation of the website main page
* */
public class ServerService implements IServerService{

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

        //Application name(The key is Link_2_The_Past, because why not?Its one of my favourite games...)
        model.put("Link_2_The_Past", "[Placeholder]");

        //Information related to the device that made the request
        model.put("OS", currentDevice.getOS());
        model.put("OS_Name", currentDevice.getOS_Friendly_Name());
        model.put("isSupported", currentDevice.isSupported());

        return "index";
    }
}
