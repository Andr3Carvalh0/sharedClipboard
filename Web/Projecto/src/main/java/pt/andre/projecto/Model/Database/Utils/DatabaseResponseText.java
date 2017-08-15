package pt.andre.projecto.Model.Database.Utils;

import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;

/*
* Represents the object of the response that we will deliver
* */
public class DatabaseResponseText extends DatabaseResponse<String> {

    private final String responseMessage;

    public DatabaseResponseText(int responseCode, String responseMessage) {
        super(responseCode);
        this.responseMessage = responseMessage;
    }

    public DatabaseResponseText(int responseCode, long responseMessage) {
        this(responseCode, responseMessage + "");
    }

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }
}