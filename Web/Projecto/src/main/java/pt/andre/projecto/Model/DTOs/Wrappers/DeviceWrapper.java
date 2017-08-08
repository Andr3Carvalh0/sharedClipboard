package pt.andre.projecto.Model.DTOs.Wrappers;


public class DeviceWrapper {
    private final String id;
    private final String name;
    private final boolean mobile;
    private final String sub;

    public DeviceWrapper(String id, String name, boolean mobile, String sub) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.sub = sub;
    }

    public String getSub() {
        return sub;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isMobile() {
        return mobile;
    }
}
