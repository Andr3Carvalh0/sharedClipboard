package pt.andre.projecto.Output;

import pt.andre.projecto.Output.Interfaces.DatabaseResponse;

/*
* Represents the object of the response that we will deliver
* */
public class DatabaseResponseMultimedia extends DatabaseResponse<byte[]> {


    private final byte[] responseContent;

    public DatabaseResponseMultimedia(int responseCode, byte[] responseContent) {
        super(responseCode);
        this.responseContent = responseContent;
    }

    @Override
    public byte[] getResponseMessage() {
        return responseContent == null ? new byte[1] : responseContent;
    }
}
