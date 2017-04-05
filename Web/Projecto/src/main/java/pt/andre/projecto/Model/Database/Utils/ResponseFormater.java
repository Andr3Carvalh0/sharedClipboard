package pt.andre.projecto.Model.Database.Utils;

/*
* Used to convert MongoDB errors to a more user friendly messages
*/
public class ResponseFormater {
    private final static int VALID_REQUEST = 200;
    private final static int AUTHENTICATION_FAILED = 403;
    private final static int ACCOUNT_CREATION_FAILED = 409;
    private final static int CONNECTION_DATABASE_FAILED = 500;

    private final static String VALID_REQUEST_MESSAGE = "Success!";
    private final static String AUTHENTICATION_FAILED_MESSAGE = "The credentials passed aren't valid.";
    private final static String ACCOUNT_CREATION_FAILED_MESSAGE = "There is already one account with this email.";
    private final static String CONNECTION_DATABASE_FAILED_MESSAGE = "Cannot process this request right now. Try later.";

    /**
     *
     * Used for error situations, or when we dont have to deliver important information to the user.
     * For instance when the user creates successfully an account we dont have to return information back to him.
     * So we can use this method just to say that the creation was successfully
     */
    public static DatabaseResponse createResponse(String message){
        if(message == null)
            return new DatabaseResponse(VALID_REQUEST, VALID_REQUEST_MESSAGE);

        if("User not valid".equals(message))
            return new DatabaseResponse(AUTHENTICATION_FAILED, AUTHENTICATION_FAILED_MESSAGE);

        if("Exception receiving message".equals(message))
            return new DatabaseResponse(CONNECTION_DATABASE_FAILED, CONNECTION_DATABASE_FAILED_MESSAGE);

        if(message.contains("duplicate key"))
            return new DatabaseResponse(ACCOUNT_CREATION_FAILED, ACCOUNT_CREATION_FAILED_MESSAGE);

        return new DatabaseResponse(VALID_REQUEST, VALID_REQUEST_MESSAGE);
    }


    /**
     *
     * Used for cases like, when we have to return information to the user
     * EG: user id, contented that he copyied, etc...
     */
    public static DatabaseResponse displayInformation(String message){
        return new DatabaseResponse(VALID_REQUEST, message);
    }

}
