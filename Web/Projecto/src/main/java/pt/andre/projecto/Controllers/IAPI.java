package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;

public interface IAPI {
    ResponseEntity push(String account);
    ResponseEntity pull(String user);
    ResponseEntity createAccount(String account, String password);
    ResponseEntity authenticate(String account, String password);
}
