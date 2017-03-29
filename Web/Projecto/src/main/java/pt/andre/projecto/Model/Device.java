package pt.andre.projecto.Model;

public class Device {

    private final String OS;
    private final String OS_Friendly;
    private final Boolean Mobile;
    private final Boolean Supported;

    public Device(String OS, String OS_Friendly_Name, Boolean Mobile, Boolean Supported){
        this.OS = OS;
        this.OS_Friendly = OS_Friendly_Name;
        this.Mobile = Mobile;
        this.Supported = Supported;
    }

    public String getOS() {
        return OS;
    }

    public String getOS_Friendly_Name() {
        return OS_Friendly;
    }

    public Boolean isMobile() {
        return Mobile;
    }

    public Boolean isSupported() {
        return Supported;
    }
}
