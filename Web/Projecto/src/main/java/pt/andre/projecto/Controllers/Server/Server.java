package pt.andre.projecto.Controllers.Server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.andre.projecto.Model.Device;
import pt.andre.projecto.Model.DeviceIdentifier;

import java.util.Map;

@Controller
public class Server {

    @RequestMapping("/")
    public String root(Map<String, Object> model, @RequestHeader(value="User-Agent") String userAgent, DeviceIdentifier deviceIdentifier) {
        Device currentDevice = deviceIdentifier.getDeviceInformation(userAgent);

        model.put("OS", currentDevice.getOS());
        model.put("OS_Name", currentDevice.getOS_Friendly_Name());

        return "index";
    }
}

