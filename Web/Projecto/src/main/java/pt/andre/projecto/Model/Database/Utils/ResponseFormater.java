package pt.andre.projecto.Model.Database.Utils;

import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;
import pt.andre.projecto.Model.Utils.DefaultHashMap;
import pt.andre.projecto.Model.Utils.MensageFormater;

/*
* Used to convert MongoDB errors to a more user friendly messages
*/
public class ResponseFormater {
    public final static int NO_ACCOUNT = 1;
    public final static int ACCOUNT_EXISTS_EXCEPTION = 2;
    public final static int EXCEPTION = 3;
    public final static int POST_EXCEPTION = 4;
    public final static int NOT_PERMITTED = 5;

    public final static int SUCCESS = 27;

    private final static int VALID_REQUEST = 200;
    private final static int NO_SUCH_ACCOUNT_EXISTS = 400;
    private final static int NO_PERMISSION = 403;
    private final static int ACCOUNT_CREATION_FAILED = 409;
    private final static int SERVER_FAILED = 500;

    private final static String VALID_REQUEST_MESSAGE = "Success!";

    private final static String NO_SUCH_ACCOUNT_EXISTS_MESSAGE = "{" +
                "\"title\":\"No account.\"," +
                "\"detail\": \"There isn't a account associated with this id.\"" +
            "}";

    private final static String ACCOUNT_CREATION_FAILED_MESSAGE = "{" +
                "\"title\":\"Already one account\"," +
                "\"detail\": \"There is already one account with this email.\""+
            "}";

    private final static String SERVER_FAILED_MESSAGE = "{" +
                "\"title\":\"Server error\"," +
                "\"detail\": \"Cannot process this request right now. Try later.\""+
            "}";

    private final static String BAD_REQUEST_MESSAGE =  "{" +
                "\"title\":\"Invalid request\"," +
                "\"detail\": \"The Request is in a invalid format.\""+
            "}";

    private final static String NO_PERMISSION_MESSAGE =  "{" +
                "\"title\":\"YOU SHALL NOT PASS!\"," +
                "\"detail\": \"You don´t have permission to access this resource.\""+
            "}";

    private static DefaultHashMap<Integer, DatabaseResponse> responses = new DefaultHashMap<>(new DatabaseResponseText(VALID_REQUEST, VALID_REQUEST_MESSAGE));


    static{
        responses.put(NO_ACCOUNT, new DatabaseResponseText(NO_SUCH_ACCOUNT_EXISTS, NO_SUCH_ACCOUNT_EXISTS_MESSAGE));
        responses.put(EXCEPTION, new DatabaseResponseText(SERVER_FAILED, SERVER_FAILED_MESSAGE));
        responses.put(ACCOUNT_EXISTS_EXCEPTION, new DatabaseResponseText(ACCOUNT_CREATION_FAILED, ACCOUNT_CREATION_FAILED_MESSAGE));
        responses.put(POST_EXCEPTION, new DatabaseResponseText(NO_SUCH_ACCOUNT_EXISTS, BAD_REQUEST_MESSAGE));
        responses.put(NOT_PERMITTED, new DatabaseResponseText(NO_PERMISSION, NO_PERMISSION_MESSAGE));

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

    public static DatabaseResponse createResponseMultimedia(int message){
        final DatabaseResponse databaseResponse = responses.get(message);

        return new DatabaseResponseMultimedia(databaseResponse.getResponseCode(), null);
    }

    /**
     * Used for cases like, when we have to return information to the user
     * EG: user id, contented that he copied, etc...
     */
    public static DatabaseResponse displayInformation(String message){
        return new DatabaseResponseText(VALID_REQUEST, message);
    }

    public static DatabaseResponse displayInformation(String message, boolean isMime){
        String res = MensageFormater.updateMessage(message, isMime);

        return new DatabaseResponseText(VALID_REQUEST, res);
    }

    public static DatabaseResponse displayInformation(byte[] content){
        return new DatabaseResponseMultimedia(VALID_REQUEST, content);
    }

}
