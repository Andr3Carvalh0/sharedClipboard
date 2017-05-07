package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;

public interface IAPI {
    ResponseEntity push(long token, String data);
    ResponseEntity pull(long token);
    ResponseEntity createAccount(String account, String password);
    ResponseEntity authenticate(String account, String password);
    ResponseEntity registerAndroidDevice(long account, String deviceID);



}
