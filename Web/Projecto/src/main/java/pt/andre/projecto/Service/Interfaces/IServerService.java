package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Model.Utils.DeviceIdentifier;

import java.util.Map;

public interface IServerService extends IParentService {
    /*
    * Responsable for returning the website UI
    */
    String handleRootRequest(Map<String, Object> model, String userAgent, DeviceIdentifier deviceIdentifier);

    String getDevices(Map<String, Object> model, String token);

    String deleteDevice(Map<String, Object> model, String sub, String deviceIdentifier, boolean isMobile);

    void notifyRemovedDevice(String sub, String deviceID, boolean isMobile);
}
