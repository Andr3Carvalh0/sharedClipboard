package pt.andre.projecto.Controllers;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IAPI {
    DatabaseResponse push(String account);
    DatabaseResponse pull(String user);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);
}
