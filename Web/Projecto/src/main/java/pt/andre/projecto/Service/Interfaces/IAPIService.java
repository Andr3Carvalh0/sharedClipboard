package pt.andre.projecto.Service.Interfaces;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;


public interface IAPIService {

    DatabaseResponse push(int token, String data);
    DatabaseResponse push(int token, String data, boolean isMIME);

    //Only used by the android client notification
    DatabaseResponse registerAndroidDevice(int token, String firebaseID);

    DatabaseResponse pull(int token);
    DatabaseResponse createAccount(String account, String password);
    DatabaseResponse authenticate(String account, String password);

}
