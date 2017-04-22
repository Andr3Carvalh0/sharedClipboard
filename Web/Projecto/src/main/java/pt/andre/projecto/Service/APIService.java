package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Andre on 22/04/2017.
 */
public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;


    @Override
    public DatabaseResponse push(String account) {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseResponse pull(String user) {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseResponse createAccount(String account, String password) {
        return database.createAccount(account, password);
    }

    @Override
    public DatabaseResponse authenticate(String account, String password) {
        return database.authenticate(account, password);
    }
}
