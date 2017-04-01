package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.MongoDB;
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
    public String push(@RequestParam String account) {
        throw new NotImplementedException();
    }

    @Override
    @RequestMapping(value = "/api/pull", params = {"account"}, method = RequestMethod.GET)
    public String pull(@RequestParam(value = "account") String user) {
        throw new NotImplementedException();
    }

    @Override
    @RequestMapping(value = "/api/account", method = RequestMethod.PUT)
    public String createAccount(@RequestParam String account, @RequestParam String password) {
        System.out.println(account);
        System.out.println(password);

        return "A";
    }

    //TODO ask which method to use on authentication
    //TODO ask if we need to verify account for every operation(check password, for instance)
    @Override
    @RequestMapping(value = "/api/account", params = {"account", "password"}, method = RequestMethod.GET)
    public String authenticate(@RequestParam(value = "account") String account, @RequestParam(value = "password") String password) {
        throw new NotImplementedException();
    }


    @Component
    @ComponentScan
    public static class Configuration{

        @Bean
        public IDatabase createDatabase(){
            return new MongoDB(System.getenv("MONGO_URL"), System.getenv("MONGO_PORT"));
        }


    }

}
