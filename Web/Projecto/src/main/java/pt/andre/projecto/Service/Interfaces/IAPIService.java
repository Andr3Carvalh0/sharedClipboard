package pt.andre.projecto.Service.Interfaces;

import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

import javax.servlet.http.HttpServletRequest;


public interface IAPIService {

    DatabaseResponse push(String sub, String data);
    DatabaseResponse push(MultipartFile file, String sub);
    DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName);
    DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName);
    DatabaseResponse pull(String sub);
    DatabaseResponse createAccount(String token);
    DatabaseResponse authenticate(String token);

}
