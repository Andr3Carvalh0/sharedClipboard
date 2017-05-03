package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

/**
 * Created by Andre on 22/04/2017.
 */
public interface IAPIService {

    DatabaseResponse push(long token, String data);

    //Only used by the android client notification
    DatabaseResponse registerAndroidDevice(long token, String firebaseID);

    DatabaseResponse pull(long token);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
