package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IAPI {


    ResponseEntity push(long token, String data, String deviceIdentifier);


    ResponseEntity push(MultipartFile file, Long token, String deviceIdentifier);


    ResponseEntity pull(long token, String deviceIdentifier);


    ResponseEntity createAccount(String account, String password);
    ResponseEntity authenticate(String account, String password);
    ResponseEntity associateDeviceWithAccount(long account, String deviceIdentifier, boolean deviceType);



}
