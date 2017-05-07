package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;


public interface IAPIService {

    DatabaseResponse push(long token, String data);
    DatabaseResponse push(long token, String data, boolean isMIME);

    //Only used by the android client notification
    DatabaseResponse registerAndroidDevice(long token, String firebaseID);

    DatabaseResponse pull(long token);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
