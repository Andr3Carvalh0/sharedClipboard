package pt.andre.projecto.Service.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;


public interface IAPIService extends IParentService {

    DatabaseResponse push(String sub, String data);
    DatabaseResponse push(MultipartFile file, String sub);
    DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName);
    DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName);
    DatabaseResponse pull(String sub);
    DatabaseResponse createAccount(String token);
    DatabaseResponse authenticate(String token);
    DatabaseResponse handleMIMERequest(String encryptedSUB, String sub, String file);
}
