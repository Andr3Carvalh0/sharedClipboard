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
import pt.andre.projecto.Model.DTOs.Content;
import pt.andre.projecto.Model.DTOs.User;
import pt.andre.projecto.Model.DTOs.Wrappers.ContentWrapper;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.DTOs.Wrappers.UserWrapper;
import pt.andre.projecto.Model.Database.Interfaces.IDatabase;
import pt.andre.projecto.Model.Database.Interfaces.IPendingRequests;
import pt.andre.projecto.Output.Interfaces.DatabaseResponse;
import pt.andre.projecto.Output.ResponseFormater;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase, IPendingRequests {

    public static final String MOBILE_CLIENTS = "mobileClients";
    private static final String DESKTOP_CLIENTS = "desktopClients";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Mongo ";

    private final MongoDatabase mongoDatabase;
    private final static DatabaseOption USER_COLLECTION = new DatabaseOption("users", "id");
    private final static DatabaseOption CONTENT_COLLECTION = new DatabaseOption("content", "id");
    private final static DatabaseOption PENDINGREQUESTS_COLLECTION = new DatabaseOption("pending", "id");
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
        initializeCollections(USER_COLLECTION, CONTENT_COLLECTION, PENDINGREQUESTS_COLLECTION);

    }

    /*
    * Adds to the user Account the device ID.
    *
    * @param token : the user account token
    * @param firebaseID: the device ID, that is return by the firebase service
    * */
    @Override
    public DatabaseResponse registerMobileDevice(String sub, String firebaseID, String deviceName) {
        return registerDevice(sub, firebaseID, MOBILE_CLIENTS, deviceName);
    }

    @Override
    public DatabaseResponse registerDesktopDevice(String sub, String deviceID, String deviceName) {
        return registerDevice(sub, deviceID, DESKTOP_CLIENTS, deviceName);
    }


    /*
    * Stores the content into the database.
    *
    * @param token : the user account token
    * @param data : the data.It can the the resource it self, or a URL to the resouce
    * @param isMIME : indicates whether @param data is a resource(false) or a URL to a resource(true)
    * */
    @Override
    public DatabaseResponse push(String sub, String data, boolean isMIME, int order) {
        return transformationToContentDatabase(sub, (wrapper, collection) -> {
            logger.info(TAG + "attempting to add content to user account, content Type:" + isMIME);

            Document document = new Document();
            document.put("id", sub);
            document.put("value", data);
            document.put("isMIME", isMIME);
            document.put("order", order);

            collection.updateOne(wrapper.getAccountFilter(), new Document("$set", document));

            return ResponseFormater.createResponse(ResponseFormater.SUCCESS, order);
        });
    }

    /*
    * Get the information that the user @param token has stores
    *
    * @param token : the user account token
    * */
    @Override
    public DatabaseResponse pull(String sub) {
        logger.info(TAG + "pulling user info");
        return transformationToContentDatabase(sub, (wrapper, collection) -> ResponseFormater.displaySuccessfulInformation( wrapper.getContent().getValue(), wrapper.getContent().isMIME()));
    }

    @Override
    public DatabaseResponse authenticate(String sub) {
        logger.info(TAG + "authenticating...");
        if(sub == null) {
            logger.info(TAG + "it's a new user");
            return ResponseFormater.createResponse(ResponseFormater.NO_ACCOUNT);
        }

        try {
            MongoCollection<Document> users = mongoDatabase.getCollection(USER_COLLECTION.getName());

            Bson accountFilter = Filters.eq("id", sub);

            List<User> userList = new LinkedList<>();

            users.find(accountFilter)
                 .map(document -> new User(document.getString("id"), (List<Document>) document.get("mobileClients"), (List<Document>) document.get("desktopClients")))
                 .into(userList);

            //If we dont find an account return immediately.We do this so we can handle login/create account on our native app
            if (userList.size() == 0) {
                logger.info(TAG + "it's a new user");
                return ResponseFormater.createResponse(ResponseFormater.NO_ACCOUNT);
            }

            logger.info(TAG + "valid user");
            HashMap<String, String> rsp = new HashMap<>();
            rsp.put("id", userList.get(0).getId());

            transformationToContentDatabase(userList.get(0).getId(), (c, co) -> {
               rsp.put("order", c.getContent().getOrder() + "");
                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });


            return ResponseFormater.displaySuccessfulInformation(rsp);

        } catch (Exception e) {
            logger.error(TAG + "cannot communicate with DB");
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public DatabaseResponse createAccount(String sub) {
        logger.info(TAG + "creating account....");

        try {
            Document document = new Document();
            document.put("id", sub);
            document.put("mobileClients", new ArrayList<BasicDBObject>());
            document.put("desktopClients", new ArrayList<BasicDBObject>());
            mongoDatabase.getCollection(USER_COLLECTION.getName()).insertOne(document);

            document = new Document();
            document.put("id", sub);
            document.put("value", FIRST_TIME_CONTENT_MESSAGE);
            document.put("isMIME", false);
            document.put("order", 0);
            mongoDatabase.getCollection(CONTENT_COLLECTION.getName()).insertOne(document);

            document = new Document();
            document.put("id", sub);
            document.put("devices", new ArrayList<BasicDBObject>());
            mongoDatabase.getCollection(PENDINGREQUESTS_COLLECTION.getName()).insertOne(document);

            return authenticate(sub);
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate key")) {
                logger.error(TAG + "account already exists.");
                return ResponseFormater.createResponse(ResponseFormater.ACCOUNT_EXISTS_EXCEPTION);
            }
            logger.error(TAG + "cannot communicate with DB");
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public List<DeviceWrapper> getMobileDevices(String sub) {
        logger.info(TAG + "getting mobile devices devices..........");

        return getDevices(sub, userWrapper -> userWrapper.getUser().getMobileClients(), true);
    }

    @Override
    public List<DeviceWrapper> getDesktopDevices(String sub) {
        logger.info(TAG + "getting desktop devices devices..........");

        return getDevices(sub, userWrapper -> userWrapper.getUser().getDesktopClients(), false);
    }

    @Override
    public boolean removeDevice(String sub, String deviceIdentifier, boolean isMobile) {
        String type = isMobile ? MOBILE_CLIENTS : DESKTOP_CLIENTS;

        try {
            transformationToUserDatabase(sub, (wrapper, collection) -> {

                logger.info(TAG + "attempting to remove device to user account");
                BasicDBObject document = new BasicDBObject("_main", deviceIdentifier);

                BasicDBObject updateCommand = new BasicDBObject("$pull", new BasicDBObject(type, document));

                collection.updateOne(wrapper.getAccountFilter(), updateCommand);

                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int updateAndGetOrder(String user) {
        try{
            transformationToContentDatabase(user, (wrapper, collection) -> {
                logger.info(TAG + "increase order");

                BasicDBObject updateCommand = new BasicDBObject("$inc", new BasicDBObject("order", 1));

                collection.updateOne(wrapper.getAccountFilter(), updateCommand);

                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });


            final int[] ret = {0};
            transformationToContentDatabase(user, (wrapper, collection) -> {
                logger.info(TAG + "returning order" + wrapper.getContent().getOrder());

                ret[0] = wrapper.getContent().getOrder();
                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });

            return ret[0];
        }catch (Exception e){
            logger.error(TAG + "Error storing updated order number");
        }

        return 0;
    }

    @Override
    public List<String> getPendingRequests(String sub, String device) {
        return null;
    }

    @Override
    public void addRequest(String sub, String device, String message) {

    }

    @Override
    public void removeAllRequests(String sub, String device) {

    }

    //Utils
    /*
    * Gets the item in the Content Table.
    * From the obtain value, e can do operations, using the @param func
    * */
    private DatabaseResponse transformationToContentDatabase(String sub, BiFunction<ContentWrapper, MongoCollection, DatabaseResponse> func) {
        try {
            final MongoCollection<Document> contentDocument = mongoDatabase.getCollection(CONTENT_COLLECTION.getName());
            Bson accountFilter = Filters.eq("id", sub);

            final Content[] content = new Content[1];

            contentDocument.find(accountFilter)
                    .forEach((Block<Document>) (
                            document) -> content[0] = new Content(document.getString("id"), document.getString("value"), document.getBoolean("isMIME"), document.getInteger("order"))
                    );

            if(content[0] == null)
                return ResponseFormater.createResponse(ResponseFormater.NO_ACCOUNT);

            return func.apply(new ContentWrapper(content[0], accountFilter), contentDocument);
        } catch (Exception e) {
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    private DatabaseResponse transformationToUserDatabase(String sub, BiFunction<UserWrapper, MongoCollection, DatabaseResponse> func) {
        try {
            final MongoCollection<Document> contentDocument = mongoDatabase.getCollection(USER_COLLECTION.getName());
            Bson accountFilter = Filters.eq("id", sub);

            final User[] user = new User[1];
            contentDocument.find(accountFilter)
                    .forEach((Block<Document>) (
                            document) -> user[0] = new User(document.getString("id"), (List<Document>) document.get("mobileClients"), (List<Document>) document.get("desktopClients"))
                    );

            return func.apply(new UserWrapper(user[0], accountFilter), contentDocument);
        } catch (Exception e) {
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    private DatabaseResponse transformationToPendingDatabase(String sub, BiFunction<UserWrapper, MongoCollection, DatabaseResponse> func) {
        throw new NotImplementedException();
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

    private List<DeviceWrapper> getDevices(String sub, Function<UserWrapper, List<Document>> func, boolean isMobile) {

        final List<Document> res = new LinkedList<>();

        transformationToUserDatabase(sub,
                (wrapper, collection) -> {
                    res.addAll(func.apply(wrapper));
                    return null;
                }
        );

        return res.stream()
                .map(document -> new DeviceWrapper(document.getString("_main"), document.getString("name"), isMobile, sub))
                .collect(Collectors.toList());
    }

    private DatabaseResponse registerDevice(String sub, String device, String type, String deviceName) {
        try {
            return transformationToUserDatabase(sub, (wrapper, collection) -> {

                logger.info(TAG + "attempting to add device to user account");
                BasicDBObject document = new BasicDBObject("main", device);
                document.append("name", deviceName);

                BasicDBObject updateCommand = new BasicDBObject("$addToSet", new BasicDBObject(type, document));

                collection.updateOne(wrapper.getAccountFilter(), updateCommand);

                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });
        } catch (Exception e) {
            logger.error(TAG + "Error on registering device");
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

}
