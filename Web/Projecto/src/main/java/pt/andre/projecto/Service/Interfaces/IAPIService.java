package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

/**
 * Created by Andre on 22/04/2017.
 */
public interface IAPIService {

    DatabaseResponse push(String account);
    DatabaseResponse pull(String user);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
