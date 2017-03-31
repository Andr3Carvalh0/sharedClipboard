package pt.andre.projecto.Model.Utils;

public class DeviceIdentifier {
    private DefaultHashMap<String, Device> devices;

    public DeviceIdentifier(){
        devices = new DefaultHashMap<>(new Device("WIN", "Windows",  true));
        devices.put("Macintosh", new Device("OSX", "Mac",  true));
        devices.put("Windows", new Device("WIN", "Windows", true));
        devices.put("iPhone", new Device("OSX", "Mac",  false));
        devices.put("iPod", new Device("OSX", "Mac", false));
        devices.put("iPad", new Device("OSX", "Mac", false));

    }

    /* TODO: Learn Regex and replace all of the split functions.
     * Based on the userAgent that we receive in the header of the request we try to figure out which plataform the user is using.
     * Eg: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.1 Safari/603.1.30
     * Based on that we can find that the user is using a Macintosh.
     */
    public Device getDeviceInformation(String userAgent){
        String OS = userAgent.split("\\(")[1].split(";")[0].split("\\s+")[0];
        return devices.get(OS);
    }

}
