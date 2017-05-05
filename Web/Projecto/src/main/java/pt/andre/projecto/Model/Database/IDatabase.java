package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IDatabase {


    DatabaseResponse registerAndroidDevice(int token, String firebaseID);
    DatabaseResponse push(int token, String data);
    DatabaseResponse push(int token, String data, boolean isMIME);
    DatabaseResponse pull(int token);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String password);

}
