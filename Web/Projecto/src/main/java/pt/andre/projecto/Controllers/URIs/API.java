package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;

@RestController
@AutoConfigureBefore
public class API implements IAPI {

    @Autowired
    IAPIService service;

    @Override
    @RequestMapping(value = "/api/push", method = RequestMethod.PUT)
    public ResponseEntity push(@RequestParam Integer token, @RequestParam String data) {
        final DatabaseResponse resp = service.push(token, data);

        return ResponseEntity.status(resp.getResponseCode()).build();
    }

    @Override
    @RequestMapping(value = "/api/pull", params = {"account"}, method = RequestMethod.GET)
    public ResponseEntity pull(@RequestParam(value = "account") Integer token) {
        final DatabaseResponse resp = service.pull(token);

        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/account", method = RequestMethod.PUT)
    public ResponseEntity createAccount(@RequestParam String account, @RequestParam String password) {
        final DatabaseResponse resp = service.createAccount(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/account", params = {"account", "password"}, method = RequestMethod.GET)
    public ResponseEntity authenticate(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        final DatabaseResponse resp = service.authenticate(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/registerAndroid", method = RequestMethod.PUT)
    public ResponseEntity registerAndroidDevice(@RequestParam Integer account, @RequestParam String deviceID) {
        final DatabaseResponse resp = service.registerAndroidDevice(account, deviceID);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

}
