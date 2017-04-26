package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;

/**
 * Created by Andre on 22/04/2017.
 */
public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;


    @Override
    public DatabaseResponse push(Integer token, String data) {
        return database.push(token, data);
    }

    @Override
    public DatabaseResponse pull(Integer token) {
        return database.pull(token);
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
