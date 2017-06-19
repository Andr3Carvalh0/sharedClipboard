package pt.andre.projecto.Service.Interfaces;

import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

import javax.servlet.http.HttpServletRequest;


public interface IAPIService {

    DatabaseResponse push(long token, String data);
    DatabaseResponse push(MultipartFile file, long token);
    DatabaseResponse registerMobileDevice(long token, String firebaseID);
    DatabaseResponse pull(long token);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
