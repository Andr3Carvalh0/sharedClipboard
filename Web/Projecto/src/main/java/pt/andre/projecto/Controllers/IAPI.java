package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;

public interface IAPI {
    ResponseEntity push(Integer token, String data);
    ResponseEntity pull(Integer token);
    ResponseEntity createAccount(String account, String password);
    ResponseEntity authenticate(String account, String password);
}
