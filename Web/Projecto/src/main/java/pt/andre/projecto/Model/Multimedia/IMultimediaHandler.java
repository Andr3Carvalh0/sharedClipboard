package pt.andre.projecto.Model.Multimedia;

import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;

public interface IMultimediaHandler {
    String store(String sub, byte[] file, String filename);
    DatabaseResponse pull(String encryptedSUB, String sub, String file);

}
