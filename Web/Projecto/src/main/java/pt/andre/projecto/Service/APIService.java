package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Controllers.URIs.FirebaseServer;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Service.Interfaces.IAPIService;

public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;

    @Autowired
    private FirebaseServer firebaseServer;


    @Override
    public DatabaseResponse push(long token, String data) {
        return this.push(token, data, false);
    }

    @Override
    public DatabaseResponse push(long token, String data, boolean isMIME) {
        DatabaseResponse push = database.push(token, data, isMIME);

        firebaseServer.notify(data, isMIME, database.getDevices(token));
        return push;
    }

    @Override
    public DatabaseResponse registerAndroidDevice(long token, String firebaseID) {
        return database.registerAndroidDevice(token, firebaseID);
    }

    @Override
    public DatabaseResponse pull(long token) {
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
