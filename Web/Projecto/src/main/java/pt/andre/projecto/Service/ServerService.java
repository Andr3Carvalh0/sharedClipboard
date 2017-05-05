package pt.andre.projecto.Service;

import pt.andre.projecto.Model.Utils.Device;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;
import pt.andre.projecto.Service.Interfaces.IServerService;

import java.util.Map;

public class ServerService implements IServerService{

    @Override
    public String handleRootRequest(Map<String, Object> model, String userAgent, DeviceIdentifier deviceIdentifier) {
        Device currentDevice = deviceIdentifier.getDeviceInformation(userAgent);

        //Application name
        model.put("Link_2_The_Past", "[Placeholder]");

        //Information related to the device that made the request
        model.put("OS", currentDevice.getOS());
        model.put("OS_Name", currentDevice.getOS_Friendly_Name());
        model.put("isSupported", currentDevice.isSupported());

        return "index";
    }
}
