package pt.andre.projecto.Model.Utils;

/*
* Represents an DeviceWrapper
* */
public class Device {
    //String contained in the userAgent String
    private final String OS;

    //String that can be showed to the user(Windows, OS X, Android, etc...)
    private final String OS_Friendly;

    //Indicates whether or not we support the OS
    private final Boolean Supported;

    Device(String OS, String OS_Friendly_Name, Boolean Supported){
        this.OS = OS;
        this.OS_Friendly = OS_Friendly_Name;
        this.Supported = Supported;
    }

    public String getOS() {
        return OS;
    }

    public String getOS_Friendly_Name() {
        return OS_Friendly;
    }

    public Boolean isSupported() {
        return Supported;
    }
}
