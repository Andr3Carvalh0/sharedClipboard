package pt.andre.projecto.Model.Utils;


public class JSONFormatter {

    public static String formatToJSON(String message, boolean isMime){
        return "{" +
                    "\"content\":\"" + message + "\",\n" +
                    "\"isMIME\": " + isMime +
                "\n}";
    }
}
