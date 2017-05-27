package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IDatabase {


    DatabaseResponse registerAndroidDevice(long token, String firebaseID);
    DatabaseResponse push(long token, String data, boolean isMIME);
    DatabaseResponse pull(long token);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String password);

    String[] getDevices(long token);
}
