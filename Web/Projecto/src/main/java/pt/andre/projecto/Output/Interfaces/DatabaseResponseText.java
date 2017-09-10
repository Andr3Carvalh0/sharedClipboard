package pt.andre.projecto.Output.Interfaces;

import pt.andre.projecto.Output.Interfaces.DatabaseResponse;

/*
* Represents the object of the response that we will deliver
* */
public class DatabaseResponseText extends DatabaseResponse<String> {

    private String responseMessage;

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

    @Override
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
