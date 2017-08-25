package pt.andre.projecto.Model.Utils;


import org.json.JSONObject;
import java.util.Base64;

public class MensageFormater {

    public static String updateMessage(String message, boolean isMime){
        JSONObject js = new JSONObject();
        js.put("action", "store");
        js.put("content", message);
        js.put("isMIME", isMime);

        return js.toString();
    }

    public static String updateMessage(byte[] file, String filename){
        String encoded_file = Base64.getEncoder().encodeToString(file);
        JSONObject js = new JSONObject();

        js.put("action", "store");
        js.put("content", encoded_file);
        js.put("filename", filename);
        js.put("isMIME", true);

        return js.toString();
    }

    public static String expel(){
        JSONObject js = new JSONObject();
        js.put("action", "remove");

        return js.toString();
    }
}
