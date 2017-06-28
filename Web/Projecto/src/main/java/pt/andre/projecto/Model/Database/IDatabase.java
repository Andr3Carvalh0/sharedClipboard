package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

import java.util.List;

public interface IDatabase {


    DatabaseResponse registerMobileDevice(long token, String firebaseID, String deviceName);
    DatabaseResponse registerDesktopDevice(long token, String deviceID, String deviceName);
    DatabaseResponse push(long token, String data, boolean isMIME);
    DatabaseResponse pull(long token);
    DatabaseResponse authenticate(String user, String pass);
    DatabaseResponse createAccount(String user, String password);

    List<DeviceWrapper> getMobileDevices(long token);
    List<DeviceWrapper> getDesktopDevices(long token);

}
