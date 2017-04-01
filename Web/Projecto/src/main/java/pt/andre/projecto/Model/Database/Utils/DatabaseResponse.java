package pt.andre.projecto.Model.Database.Utils;

public class DatabaseResponse {

    private final int responseCode;
    private final String responseMessage;

    public DatabaseResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage(){
        return responseMessage;
    }

    public int getResponseCode(){
        return responseCode;
    }

}
