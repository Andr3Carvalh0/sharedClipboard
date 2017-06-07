package pt.andre.projecto.Model.Database.Utils;

/*
* Represents the object of the response that we will deliver
* */
public class DatabaseResponse {

    //The HTTP code
    private final int responseCode;

    //The message.This is normally a user friedly message that can be displayed to the user without a problem
    private final String responseMessage;

    public DatabaseResponse(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public DatabaseResponse(int responseCode, long responseMessage) {
        this(responseCode, responseMessage + "");
    }

    public String getResponseMessage(){
        return responseMessage;
    }

    public int getResponseCode(){
        return responseCode;
    }

}
