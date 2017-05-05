package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;

public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;


    @Override
    public DatabaseResponse push(int token, String data) {
        return database.push(token, data);
    }

    @Override
    public DatabaseResponse push(int token, String data, boolean isMIME) {
        return database.push(token, data, isMIME);
    }

    @Override
    public DatabaseResponse registerAndroidDevice(int token, String firebaseID) {
        return database.registerAndroidDevice(token, firebaseID);
    }

    @Override
    public DatabaseResponse pull(int token) {
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
