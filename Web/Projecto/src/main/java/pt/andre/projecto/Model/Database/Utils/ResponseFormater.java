package pt.andre.projecto.Model.Database.Utils;

import pt.andre.projecto.Model.Utils.DefaultHashMap;

/*
* Used to convert MongoDB errors to a more user friendly messages
*/
public class ResponseFormater {
    private final static int VALID_REQUEST = 200;
    private final static int NO_SUCH_ACCOUNT_EXISTS = 400;
    private final static int PASSWORD_AUTHENTICATION_FAILED = 403;
    private final static int ACCOUNT_CREATION_FAILED = 409;
    private final static int CONNECTION_DATABASE_FAILED = 500;

    private final static String VALID_REQUEST_MESSAGE = "Success!";
    private final static String NO_SUCH_ACCOUNT_EXISTS_MESSAGE = "There isn't a account associated with this email.";
    private final static String PASSWORD_AUTHENTICATION_FAILED_MESSAGE = "The credentials passed aren't valid.";
    private final static String ACCOUNT_CREATION_FAILED_MESSAGE = "There is already one account with this email.";
    private final static String CONNECTION_DATABASE_FAILED_MESSAGE = "Cannot process this request right now. Try later.";

    private static DefaultHashMap<String, DatabaseResponse> responses = new DefaultHashMap<>(new DatabaseResponse(VALID_REQUEST, VALID_REQUEST_MESSAGE));


    static{
        responses.put("No such account.", new DatabaseResponse(NO_SUCH_ACCOUNT_EXISTS, NO_SUCH_ACCOUNT_EXISTS_MESSAGE));
        responses.put("Password not valid.", new DatabaseResponse(PASSWORD_AUTHENTICATION_FAILED, PASSWORD_AUTHENTICATION_FAILED_MESSAGE));
        responses.put("Exception receiving message", new DatabaseResponse(CONNECTION_DATABASE_FAILED, CONNECTION_DATABASE_FAILED_MESSAGE));

    }

    /**
     *
     * Used for error situations, or when we dont have to deliver important information to the user.
     * For instance when the user creates successfully an account we dont have to return information back to him.
     * So we can use this method just to say that the creation was successfully
     */
    public static DatabaseResponse createResponse(String message){
        if(message != null && message.contains("duplicate key"))
            return new DatabaseResponse(ACCOUNT_CREATION_FAILED, ACCOUNT_CREATION_FAILED_MESSAGE);

        return responses.get(message);
    }


    /**
     *
     * Used for cases like, when we have to return information to the user
     * EG: user id, contented that he copyied, etc...
     */
    public static DatabaseResponse displayInformation(String message){
        return new DatabaseResponse(VALID_REQUEST, message);
    }

    public static DatabaseResponse displayInformation(long message){
        return new DatabaseResponse(VALID_REQUEST, message);
    }

}
