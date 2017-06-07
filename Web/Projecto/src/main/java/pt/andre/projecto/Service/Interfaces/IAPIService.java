package pt.andre.projecto.Service.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;


public interface IAPIService {

    DatabaseResponse push(long token, String data);
    DatabaseResponse push(long token, MultipartFile data);
    DatabaseResponse registerMobileDevice(long token, String firebaseID);
    DatabaseResponse pull(long token);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
