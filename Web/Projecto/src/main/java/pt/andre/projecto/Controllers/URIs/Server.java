package pt.andre.projecto.Controllers.URIs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.andre.projecto.Model.Utils.Device;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;
import pt.andre.projecto.Service.Interfaces.IServerService;

import java.util.Map;

@Controller
@AutoConfigureBefore
/*
* Controller to handle the web interaction.
* This interaction allows the download of the App and the creation of an user account
* */
public class Server{

    @Autowired
    IServerService service;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Server ";

    /*
    * Main URL
    *
    * @param model: Used to handle the UI
    * @param userAgent: the user agent of the device that is accessing the website
    * @param deviceIdentifies: Object that knows how to transform an userAgent into a Object DeviceWrapper that we know how to handle
    * */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Map<String, Object> model, @RequestHeader(value = "User-Agent") String userAgent, DeviceIdentifier deviceIdentifier) {
        logger.info(TAG + "Requested Main");
        return service.handleRootRequest(model, userAgent, deviceIdentifier);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public String getDevices(Map<String, Object> model, @RequestHeader("Authorization") String token){
        return service.getDevices(model, token);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.DELETE)
    public String removeDevices(Map<String, Object> model, @RequestHeader("Authorization") String token, @RequestParam String deviceIdentifier, @RequestParam boolean isMobile){
        return service.deleteDevice(model, token, deviceIdentifier, isMobile);
    }

}


