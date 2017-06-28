package pt.andre.projecto.Service.Interfaces;

import org.springframework.web.bind.annotation.RequestHeader;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;

import java.util.Map;

public interface IServerService {
    /*
    * Responsable for returning the website UI
    */
    String handleRootRequest(Map<String, Object> model, @RequestHeader(value = "User-Agent") String userAgent, DeviceIdentifier deviceIdentifier);

    String getDevices(Map<String, Object> model, long token);
}
