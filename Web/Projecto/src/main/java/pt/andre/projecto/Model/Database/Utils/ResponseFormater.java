package pt.andre.projecto.Model.Database.Utils;

import pt.andre.projecto.Model.Utils.DefaultHashMap;

/*
* Used to convert MongoDB errors to a more user friendly messages
*/
public class ResponseFormater {
    public final static int NO_ACCOUNT = 1;
    public final static int PASSWORD_INVALID = 2;
    public final static int ACCOUNT_EXISTS_EXCEPTION = 3;
    public final static int EXCEPTION = 4;
    public final static int POST_EXCEPTION = 5;

    public final static int SUCCESS = 27;


    private final static int VALID_REQUEST = 200;
    private final static int NO_SUCH_ACCOUNT_EXISTS = 400;
    private final static int PASSWORD_AUTHENTICATION_FAILED = 403;
    private final static int ACCOUNT_CREATION_FAILED = 409;
    private final static int SERVER_FAILED = 500;

    private final static String VALID_REQUEST_MESSAGE = "Success!";
    private final static String NO_SUCH_ACCOUNT_EXISTS_MESSAGE = "There isn't a account associated with this email.";
    private final static String PASSWORD_AUTHENTICATION_FAILED_MESSAGE = "The credentials passed aren't valid.";
    private final static String ACCOUNT_CREATION_FAILED_MESSAGE = "There is already one account with this email.";
    private final static String SERVER_FAILED_MESSAGE = "Cannot process this request right now. Try later.";
    private final static String BAD_REQUEST_MESSAGE = "The Request is in a invalid format.";

    private static DefaultHashMap<Integer, DatabaseResponse> responses = new DefaultHashMap<>(new DatabaseResponse(VALID_REQUEST, VALID_REQUEST_MESSAGE));


    static{
        responses.put(NO_ACCOUNT, new DatabaseResponse(NO_SUCH_ACCOUNT_EXISTS, NO_SUCH_ACCOUNT_EXISTS_MESSAGE));
        responses.put(PASSWORD_INVALID, new DatabaseResponse(PASSWORD_AUTHENTICATION_FAILED, PASSWORD_AUTHENTICATION_FAILED_MESSAGE));
        responses.put(EXCEPTION, new DatabaseResponse(SERVER_FAILED, SERVER_FAILED_MESSAGE));
        responses.put(ACCOUNT_EXISTS_EXCEPTION, new DatabaseResponse(ACCOUNT_CREATION_FAILED, ACCOUNT_CREATION_FAILED_MESSAGE));
        responses.put(POST_EXCEPTION, new DatabaseResponse(NO_SUCH_ACCOUNT_EXISTS, BAD_REQUEST_MESSAGE));

    }

    /**
     * Used for error situations, or when we dont have to deliver important information to the user.
     * For instance when the user creates successfully an account we dont have to return information back to him.
     * So we can use this method just to say that the creation was successfully
     *
     * @param message : HTTP code
     */
    public static DatabaseResponse createResponse(int message){
        return responses.get(message);
    }


    /**
     * Used for cases like, when we have to return information to the user
     * EG: user id, contented that he copied, etc...
     */
    public static DatabaseResponse displayInformation(String message){
        return new DatabaseResponse(VALID_REQUEST, message);
    }

    /*
    * Overload of the method above.
    * Same function, diferent input
    * */
    public static DatabaseResponse displayInformation(long message){
        return new DatabaseResponse(VALID_REQUEST, message);
    }

}
