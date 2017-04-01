package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IDatabase {

    DatabaseResponse push(String user, String data);
    DatabaseResponse pull(String user);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String pass);

}
