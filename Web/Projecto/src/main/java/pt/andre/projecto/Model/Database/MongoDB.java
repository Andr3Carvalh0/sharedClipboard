package pt.andre.projecto.Model.Database;

import com.google.common.collect.Iterables;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
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


//TODO: Handle exceptions related to db(when we cannot connect to db, etc...

/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase {

    private final MongoDatabase mongoDatabase;
    private final FirebaseServer firebaseServer;
    private final static DatabaseOption USER_COLLECTION = new DatabaseOption("users", "email");
    private final static DatabaseOption CONTENT_COLLECTION = new DatabaseOption("content", "id");
    private final static String FIRST_TIME_CONTENT_MESSAGE = "Welcome!";


    public MongoDB(String host, String port, String database) {
        this(host, port, database, null, null);

    }

    /**
     * Constructor to be used when the MongoDB has authentication.
     * In case of a simple install of the mongoDB, and you dont need to authenticate use the above constructor
     */
    public MongoDB(String host, String port, String database, String user, String password) {
        Objects.requireNonNull(host, "The Host of the database cannot be null!");
        Objects.requireNonNull(port, "The Port of the database cannot be null!");
        Objects.requireNonNull(database, "The Database cannot be null!");

        firebaseServer = new FirebaseServer();

        MongoClientURI uri;

        if (user == null) {
            uri = new MongoClientURI("mongodb://" + host + ":" + port + "/" + database);
        } else {
            uri = new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + database);
        }

        MongoClient mongoClient = new MongoClient(uri);

        mongoDatabase = mongoClient.getDatabase(uri.getDatabase());
        initializeCollections(USER_COLLECTION, CONTENT_COLLECTION);

    }

    @Override
    public DatabaseResponse registerAndroidDevice(long token, String firebaseID) {
        try{
            return updateContentDatabase(token, (wrapper, collection) -> {

                BasicDBObject document = new BasicDBObject("_main", firebaseID);

                BasicDBObject updateCommand = new BasicDBObject("$addToSet", new BasicDBObject("androidClients", document));

                collection.updateOne(wrapper.getAccountFilter(), updateCommand);

                return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
            });
        }catch (Exception e){
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public DatabaseResponse push(long token, String data, boolean isMIME) {
        return updateContentDatabase(token, (wrapper, collection) -> {

            Document document = new Document();
            document.put("id", token);
            document.put("value", data);
            document.put("isMIME", isMIME);
            document.put("androidClients", wrapper.getContent().getAndroidClients());

            collection.updateOne(wrapper.getAccountFilter(), new Document("$set", document));

            return ResponseFormater.createResponse(ResponseFormater.SUCCESS);
        });
    }

    @Override
    public DatabaseResponse pull(long token) {
        return updateContentDatabase(token, (wrapper, collection) -> ResponseFormater.displayInformation(wrapper.getContent().getValue()));
    }

    @Override
    public DatabaseResponse authenticate(String user, String password) {

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
                return ResponseFormater.createResponse(ResponseFormater.NO_ACCOUNT);
            }

            if (hashedPassword.equals(userList.get(0).getPassword()))
                return ResponseFormater.displayInformation(userList.get(0).getId());

            return ResponseFormater.createResponse(ResponseFormater.PASSWORD_INVALID);

        } catch (Exception e) {
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    @Override
    public DatabaseResponse createAccount(String user, String password) {
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
            document.put("androidClients", new ArrayList<BasicDBObject>());
            mongoDatabase.getCollection(CONTENT_COLLECTION.getName()).insertOne(document);


            return authenticate(user, password);
        } catch (Exception e) {
            if(e.getMessage().contains("duplicate key"))
                return ResponseFormater.createResponse(ResponseFormater.ACCOUNT_EXISTS_EXCEPTION);

            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

    /**
     * Checks if the default collections are created.If not, delegates the creation of the same
     */
    private void initializeCollections(DatabaseOption... collections) {

        String[] existingCollections = Iterables.toArray(getExistingCollections(), String.class);

        Arrays.stream(collections)
                .filter(collection -> checkIfCollectionAlreadyExists(collection.getName(), Arrays.stream(existingCollections)))
                .forEach(this::createCollection);

    }

    /**
     * Handles the creation of one collection @param collection
     */
    private void createCollection(DatabaseOption collection) {
        mongoDatabase.createCollection(collection.getName());
        mongoDatabase.getCollection(collection.getName()).createIndex(new Document(collection.getPrimaryKey(), 1), new IndexOptions().unique(true));
    }

    /**
     * Checks if the collection named @param collection exists.
     *
     * @param existingCollections the existing collections
     */
    private boolean checkIfCollectionAlreadyExists(String collection, Stream<String> existingCollections) {
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

    private DatabaseResponse updateContentDatabase(long token, BiFunction<ContentWrapper, MongoCollection, DatabaseResponse> func) {
        try {
            final MongoCollection<Document> contentDocument = mongoDatabase.getCollection(CONTENT_COLLECTION.getName());
            Bson accountFilter = Filters.eq("id", token);

            final Content[] content = new Content[1];
            contentDocument.find(accountFilter)
                    .forEach((Block<Document>) (
                            document) -> content[0] = new Content(document.getLong("id"), document.getString("value"), document.getBoolean("isMIME"), (List<BasicDBObject>)document.get("androidClients"))
                    );

            return func.apply(new ContentWrapper(content[0], accountFilter), contentDocument);
        }catch (Exception e){
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);
        }
    }

}
