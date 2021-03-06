package pt.andre.projecto.Output.Interfaces;

/*
* Represents the object of the response that we will deliver
* */
public abstract class DatabaseResponse<T> {

    //The HTTP code
    private final int responseCode;


    public DatabaseResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode(){
        return responseCode;
    }

    public abstract T getResponseMessage();
    public abstract void setResponseMessage(T newContent);

}
