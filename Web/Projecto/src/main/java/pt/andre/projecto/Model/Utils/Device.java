package pt.andre.projecto.Model.Utils;

public class Device {

    private final String OS;
    private final String OS_Friendly;

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
