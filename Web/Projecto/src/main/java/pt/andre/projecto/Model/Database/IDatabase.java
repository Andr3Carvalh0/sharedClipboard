package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IDatabase {

    DatabaseResponse push(long token, String data);
    DatabaseResponse pull(long token);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String password);

}
