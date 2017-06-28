package pt.andre.projecto.Model.DTOs.Wrappers;

/**
 * Created by André Carvalho on 28/06/2017.
 */
public class DeviceWrapper {
    private final String id;
    private final String name;
    private final boolean mobile;

    public DeviceWrapper(String id, String name, boolean mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
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
