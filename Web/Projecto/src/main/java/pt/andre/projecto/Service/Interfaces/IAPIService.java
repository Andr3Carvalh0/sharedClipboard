package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Output.Interfaces.DatabaseResponse;

public interface IAPIService extends IParentService {

    DatabaseResponse push(String sub, String data, String device);
    DatabaseResponse push(String sub, byte[] file, String filename, String device);
    DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName);
    DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName);
    DatabaseResponse pull(String sub);
    DatabaseResponse createAccount(String token);
    DatabaseResponse authenticate(String token);
    DatabaseResponse handleMIMERequest(String encryptedSUB, String sub, String file);

    DatabaseResponse getSocket();

}
