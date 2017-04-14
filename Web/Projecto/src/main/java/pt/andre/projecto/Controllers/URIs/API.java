package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@AutoConfigureBefore
public class API implements IAPI {

    private final IDatabase database;

    @Autowired
    public API(IDatabase database) {
        this.database = database;

    }

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
        DatabaseResponse resp = database.createAccount(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Override
    @RequestMapping(value = "/api/account", params = {"account", "password"}, method = RequestMethod.GET)
    public ResponseEntity authenticate(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        DatabaseResponse resp = database.authenticate(account, password);
        return ResponseEntity.status(resp.getResponseCode()).body(resp.getResponseMessage());
    }

    @Component
    @ComponentScan
    public static class Configuration{

        @Bean
        @Scope("prototype") //By default is singleton, but can be request(To be request we have to change the component scope or create a proxy on this bean) or prototype
        public IDatabase createDatabase(){
            return System.getenv("MONGO_USER") == null ? new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), "Projecto") : new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), System.getenv("MONGO_DATABASE"), System.getenv("MONGO_USER"), System.getenv("MONGO_PASSWORD"));
        }
    }
}
