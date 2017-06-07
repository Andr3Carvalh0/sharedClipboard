package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)

    /*
    * Main URL
    *
    * @param model: Used to handle the UI
    * @param userAgent: the user agent of the device that is accessing the website
    * @param deviceIdentifies: Object that knows how to transform an userAgent into a Object Device that we know how to handle
    * */
    public String root(Map<String, Object> model, @RequestHeader(value = "User-Agent") String userAgent, DeviceIdentifier deviceIdentifier) {
        return service.handleRootRequest(model, userAgent, deviceIdentifier);
    }

}


