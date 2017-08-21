package pt.andre.projecto.Model.Utils;


import org.json.JSONObject;
import java.util.Base64;

public class JSONFormatter {

    public static String formatToJSON(String message, boolean isMime){
        JSONObject js = new JSONObject();
        js.put("content", message);
        js.put("isMIME", isMime);

        return js.toString();
    }

    public static String formatToJSON(byte[] file, String filename){
        String encoded_file = Base64.getEncoder().encodeToString(file);
        JSONObject js = new JSONObject();

        js.put("content", encoded_file);
        js.put("filename", filename);

        return js.toString();
    }
}
