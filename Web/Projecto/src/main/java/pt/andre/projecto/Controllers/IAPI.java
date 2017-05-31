package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IAPI {
    ResponseEntity push(long token, String data);
    ResponseEntity push(MultipartFile file, long token);
    ResponseEntity pull(long token);
    ResponseEntity createAccount(String account, String password);
    ResponseEntity authenticate(String account, String password);
    ResponseEntity registerAndroidDevice(long account, String deviceID);



}
