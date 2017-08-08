package pt.andre.projecto.Model.Database;

import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;

import java.util.List;

public interface IDatabase {
    DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName);
    DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName);

    DatabaseResponse push(String sub, String data, boolean isMIME);
    DatabaseResponse pull(String sub);

    DatabaseResponse authenticate(String user_sub);
    DatabaseResponse createAccount(String user_sub);

    List<DeviceWrapper> getMobileDevices(String sub);
    List<DeviceWrapper> getDesktopDevices(String sub);

    boolean removeDevice(String sub, String deviceIdentifier, boolean isMobile);
}
