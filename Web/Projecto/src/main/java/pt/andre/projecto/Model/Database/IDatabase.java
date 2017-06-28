package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

public interface IDatabase {


    DatabaseResponse registerMobileDevice(long token, String firebaseID);
    DatabaseResponse registerDesktopDevice(long token, String deviceID);
    DatabaseResponse push(long token, String data, boolean isMIME);
    DatabaseResponse pull(long token);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String password);

    String[] getMobileDevices(long token);
    String[] getDesktopDevices(long token);

}
