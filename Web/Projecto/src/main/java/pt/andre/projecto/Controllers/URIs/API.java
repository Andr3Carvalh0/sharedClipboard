package pt.andre.projecto.Controllers.URIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.andre.projecto.Controllers.IAPI;
import pt.andre.projecto.Model.Database.IDatabase;

@RestController
@AutoConfigureBefore
@ComponentScan
public class API implements IAPI {

    private final IDatabase database;

    @Autowired
    public API(@Qualifier("mongoDB") IDatabase database) {
        this.database = database;
        System.out.println("OLA");
    }

    @Override
    @RequestMapping("/api/push")
    public void push() {
        database.push("A", "A");
    }

    @Override
    public void pull() {

    }

    @Override
    public void createUser() {

    }
}
