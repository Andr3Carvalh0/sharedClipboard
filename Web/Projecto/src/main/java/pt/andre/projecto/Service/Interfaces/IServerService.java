package pt.andre.projecto.Service.Interfaces;

import org.springframework.web.bind.annotation.RequestHeader;
import pt.andre.projecto.Model.Utils.DeviceIdentifier;

import java.util.Map;

/**
 * Created by Andre on 22/04/2017.
 */
public interface IServerService {

    String handleRootRequest(Map<String, Object> model, @RequestHeader(value = "User-Agent") String userAgent, DeviceIdentifier deviceIdentifier);
}
