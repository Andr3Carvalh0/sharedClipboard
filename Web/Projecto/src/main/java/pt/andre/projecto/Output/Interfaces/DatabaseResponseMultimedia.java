package pt.andre.projecto.Output.Interfaces;

/*
* Represents the object of the response that we will deliver
* */
public class DatabaseResponseMultimedia extends DatabaseResponse<byte[]> {


    private byte[] responseContent;

    public DatabaseResponseMultimedia(int responseCode, byte[] responseContent) {
        super(responseCode);
        this.responseContent = responseContent;
    }

    @Override
    public byte[] getResponseMessage() {
        return responseContent == null ? new byte[1] : responseContent;
    }

    @Override
    public void setResponseMessage(byte[] newContent) {
        this.responseContent = newContent;
    }
}
