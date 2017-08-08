package pt.andre.projecto.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IAPI {


    ResponseEntity push(long sub, String data);
    ResponseEntity push(MultipartFile file, long sub);
    ResponseEntity pull(long sub);
    ResponseEntity createAccount(String token);
    ResponseEntity authenticate(String token);
    ResponseEntity associateDeviceWithAccount(long sub, String deviceIdentifier, boolean deviceType, String deviceName);



}
