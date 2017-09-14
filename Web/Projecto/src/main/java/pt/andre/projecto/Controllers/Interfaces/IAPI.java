package pt.andre.projecto.Controllers.Interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IAPI {
    ResponseEntity push(String token, String data, String device);
    ResponseEntity push(MultipartFile file, String token, String device);
    ResponseEntity push(String token, byte[] file, String filename, String device);
    ResponseEntity pull(String token);
    ResponseEntity createAccount(String token);
    ResponseEntity authenticate(String token);
    ResponseEntity associateDeviceWithAccount(String token, String deviceIdentifier, boolean deviceType, String deviceName);
    ResponseEntity serveMIME(String encryptedSUB, String sub, String file);
    ResponseEntity getWebSocketPort();
}
