package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@AutoConfigureBefore
public class API implements IAPI {

    @Autowired
    IAPIService service;

    @Override
    @RequestMapping(value = "/api/push", method = RequestMethod.PUT)
    public ResponseEntity push(@RequestParam String account) {
        throw new NotImplementedException();
    }

    @Override
    @RequestMapping(value = "/api/pull", params = {"account"}, method = RequestMethod.GET)
    public ResponseEntity pull(@RequestParam(value = "account") String user) {
        throw new NotImplementedException();
    }

    @Override
    @RequestMapping(value = "/api/account", method = RequestMethod.PUT)
    public ResponseEntity createAccount(@RequestParam String account, @RequestParam String password) {
        DatabaseResponse resp = service.createAccount(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/account", params = {"account", "password"}, method = RequestMethod.GET)
    public ResponseEntity authenticate(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        DatabaseResponse resp = service.authenticate(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

}
