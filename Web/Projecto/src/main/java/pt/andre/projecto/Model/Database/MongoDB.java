package pt.andre.projecto.Model.Database;

import com.google.common.collect.Iterables;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.andre.projecto.Controllers.URIs.FirebaseServer;
import pt.andre.projecto.Model.DTOs.Content;
import pt.andre.projecto.Model.DTOs.User;
import pt.andre.projecto.Model.DTOs.ContentWrapper;
import pt.andre.projecto.Model.Database.Utils.DatabaseOption;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Model.Utils.Security;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Mongo ";

    private final MongoDatabase mongoDatabase;
    private final static DatabaseOption USER_COLLECTION = new DatabaseOption("users", "email");
    private final static DatabaseOption CONTENT_COLLECTION = new DatabaseOption("content", "id");
    private final static String FIRST_TIME_CONTENT_MESSAGE = "Welcome!";


    /*
    * Default contructor.Used to debug server on the local machine
    * */
    public MongoDB(String host, String port, String database) {
        this(host, port, database, null, null);
    }

    /**
     * Constructor to be used when the MongoDB has authentication.
     * In case of a simple install of the MongoDB, and you dont need to be authenticated use the above constructor
     */
    public MongoDB(String host, String port, String database, String user, String password) {
        Objects.requireNonNull(host, "The Host of the database cannot be null!");
        Objects.requireNonNull(port, "The Port of the database cannot be null!");
        Objects.requireNonNull(database, "The Database cannot be null!");

        MongoClientURI uri;

        if (user == null) {
            logger.info(TAG + "the database doesnt have authentication");
            uri = new MongoClientURI("mongodb://" + host + ":" + port + "/" + database);
        } else {
            logger.info(TAG + "the database does have authentication");
            uri = new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database);
        }

        MongoClient mongoClient = new MongoClient(uri);

        mongoDatabase = mongoClient.getDatabase(uri.getDatabase());
        initializeCollections(USER_COLLECTION, CONTENT_COLLECTION);

    }

    /*
    * Adds to the user Account the device ID.
    *
    * @param token : the user account token
    * @param firebaseID: the device ID, that is return by the firebase service
    * */
    @Override
    public DatabaseResponse registerMobileDevice(long token, String firebaseID) {
        try{
            return updateContentDatabase(token, (wrapper, collection) -> {

                logger.info(TAG + "attempting to add device to user account");
                BasicDBObject document = new BasicDBObject("_main", firebaseID);

                BasicDBObject updateCommand = new BasicDBObject("$addToSet", new BasicDBObject("mobileClients", document));

                collection.updateOne(wrapper.getAccountFilter(), updateCommand);

                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });
        }catch (Exception e){
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    /*
    * Stores the content into the database.
    *
    * @param token : the user account token
    * @param data : the data.It can the the resource it self, or a URL to the resouce
    * @param isMIME : indicates whether @param data is a resource(false) or a URL to a resource(true)
    * */
    @Override
    public DatabaseResponse push(long token, String data, boolean isMIME) {
        return updateContentDatabase(token, (wrapper, collection) -> {
            logger.info(TAG + "attempting to add content to user account, content Type:" + isMIME);

            Document document = new Document();
            document.put("id", token);
            document.put("value", data);
            document.put("isMIME", isMIME);
            document.put("mobileClients", wrapper.getContent().getMobileClients());

            collection.updateOne(wrapper.getAccountFilter(), new Document("$set", document));

            return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
        });
    }

    /*
    * Get the information that the user @param token has stores
    *
    * @param token : the user account token
    * */
    @Override
    public DatabaseResponse pull(long token) {
        logger.info(TAG + "pulling user info");
        return updateContentDatabase(token, (wrapper, collection) -> ResponseFormater.displayInformation(
                        "{" +
                            "content: '" + wrapper.getContent().getValue() + "'," +
                            "isMIME: " + wrapper.getContent().isMIME() +
                        "}"
        ));
    }

    @Override
    public DatabaseResponse authenticate(String user, String password) {
        logger.info(TAG + "authenticating...");
        try {
            String hashedPassword = Security.hashString(password);

            MongoCollection<Document> users = mongoDatabase.getCollection(USER_COLLECTION.getName());

            Bson accountFilter = Filters.eq("email", user);

            List<User> userList = new LinkedList<>();

            users.find(accountFilter)
                    .forEach((Block<Document>) (
                            document) -> userList.add(new User(document.getLong("id"), document.getString("email"), document.getString("password")))
            );

            //If we dont find an account return immediately.We do this so we can handle login/create account on our native app
            if (userList.size() == 0) {
                logger.info(TAG + "it's a new user");
                return ResponseFormater.createResponse(ResponseFormater.NO_ACCOUNT);
            }

            if (hashedPassword.equals(userList.get(0).getPassword())) {
                logger.info(TAG + "valid user");
                return ResponseFormater.displayInformation(userList.get(0).getId());

            }

            logger.info(TAG + "password didnt match");
            return ResponseFormater.createResponse(ResponseFormater.PASSWORD_INVALID);

        } catch (Exception e) {
            logger.error(TAG + "cannot communicate with DB");
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public DatabaseResponse createAccount(String user, String password) {
        logger.info(TAG + "creating account....");
        String hashedPassword = Security.hashString(password);

        try {
            long id = mongoDatabase.getCollection(USER_COLLECTION.getName()).count() + 1;

            Document document = new Document();
            document.put("id", id);
            document.put("email", user);
            document.put("password", hashedPassword);
            mongoDatabase.getCollection(USER_COLLECTION.getName()).insertOne(document);

            document = new Document();
            document.put("id", id);
            document.put("value", FIRST_TIME_CONTENT_MESSAGE);
            document.put("isMIME", false);
            document.put("mobileClients", new ArrayList<BasicDBObject>());
            mongoDatabase.getCollection(CONTENT_COLLECTION.getName()).insertOne(document);


            return authenticate(user, password);
        } catch (Exception e) {
            if(e.getMessage().contains("duplicate key")) {
                logger.error(TAG + "account already exists.");
                return ResponseFormater.createResponse(ResponseFormater.ACCOUNT_EXISTS_EXCEPTION);
            }
            logger.error(TAG + "cannot communicate with DB");
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public String[] getDevices(long token) {
        logger.info(TAG + "getting devices..........");
        final List<Document> res = new LinkedList<>();
        List<String> toReturn = new LinkedList<>();

        updateContentDatabase(token,
                (wrapper, collection) -> {
                    res.addAll(wrapper.getContent().getMobileClients());
                    return null;
            }
        );

        res.stream().forEach(s -> toReturn.add(s.getString("_main")));

        return toReturn.stream().toArray(String[]::new);
    }

    /**
     * Checks if the default collections are created.If not, delegates the creation of the same
     */
    private void initializeCollections(DatabaseOption... collections) {
        logger.info(TAG + "initializing Collection");

        String[] existingCollections = Iterables.toArray(getExistingCollections(), String.class);

        Arrays.stream(collections)
                .filter(collection -> checkIfCollectionAlreadyExists(collection.getName(), Arrays.stream(existingCollections)))
                .forEach(this::createCollection);

    }

    /**
     * Handles the creation of one collection @param collection
     */
    private void createCollection(DatabaseOption collection) {
        logger.info(TAG + "creating Collection");
        mongoDatabase.createCollection(collection.getName());
        mongoDatabase.getCollection(collection.getName()).createIndex(new Document(collection.getPrimaryKey(), 1), new IndexOptions().unique(true));
    }

    /**
     * Checks if the collection named @param collection exists.
     *
     * @param existingCollections : the existing collections
     */
    private boolean checkIfCollectionAlreadyExists(String collection, Stream<String> existingCollections) {
        logger.info(TAG + "checking if Collection:" + collection + " exists");

        return existingCollections
                .filter(collection::equals)
                .count() == 0;
    }

    private Iterable<String> getExistingCollections() {
        return mongoDatabase.listCollectionNames();
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    /*
    * Gets the item in the Content Table.
    * From the obtain value, e can do operations, using the @param func
    * @todo: Rename this method since the pull method also uses this methods, and with this name can be confusing.
    * */
    private DatabaseResponse updateContentDatabase(long token, BiFunction<ContentWrapper, MongoCollection, DatabaseResponse> func) {
        try {
            final MongoCollection<Document> contentDocument = mongoDatabase.getCollection(CONTENT_COLLECTION.getName());
            Bson accountFilter = Filters.eq("id", token);

            final Content[] content = new Content[1];
            contentDocument.find(accountFilter)
                    .forEach((Block<Document>) (
                            document) -> content[0] = new Content(document.getLong("id"), document.getString("value"), document.getBoolean("isMIME"), (List<Document>) document.get("mobileClients"))
                    );

            return func.apply(new ContentWrapper(content[0], accountFilter), contentDocument);
        }catch (Exception e){
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

}
