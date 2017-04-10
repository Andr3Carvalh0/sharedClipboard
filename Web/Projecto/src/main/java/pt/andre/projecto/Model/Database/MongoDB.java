package pt.andre.projecto.Model.Database;

import com.google.common.collect.Iterables;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import pt.andre.projecto.Model.Database.Utils.DatabaseOption;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Model.Utils.Security;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.*;
import java.util.stream.Stream;


//TODO: Handle exceptions related to db(when we cannot connect to db, etc...

/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase {

    private final MongoDatabase mongoDatabase;
    private final static DatabaseOption USER_COLLECTION = new DatabaseOption("users", "email");
    private final static DatabaseOption CONTENT_COLLECTION = new DatabaseOption("content", "id");
    private final static String FIRST_TIME_CONTENT_MESSAGE = "Welcome!";


    public MongoDB(String Host, String Port, String Database) {
        this(Host, Port, Database, null, null);

    }

    /**
     * Constructor to be used when the MongoDB has authentication.
     * In case of a simple install of the mongoDB, and you dont need to authenticate use the above constructor
     */
    public MongoDB(String Host, String Port, String Database, String User, String Password) {
        Objects.requireNonNull(Host, "The Host of the database cannot be null!");
        Objects.requireNonNull(Port, "The Port of the database cannot be null!");
        Objects.requireNonNull(Database, "The Database cannot be null!");

        MongoClientURI uri;

        if(User == null) {
            uri = new MongoClientURI("mongodb://" + Host + ":" + Port + "/" + Database);
        }else{
            uri = new MongoClientURI("mongodb://" + User + ":" + Password + "@" + Host + ":" + Port + "/" + Database);
        }

        MongoClient mongoClient = new MongoClient(uri);

        mongoDatabase = mongoClient.getDatabase(uri.getDatabase());
        initializeCollections(USER_COLLECTION, CONTENT_COLLECTION);

    }


    @Override
    public DatabaseResponse push(String user, String data) {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseResponse pull(String user) {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseResponse authenticate(String user, String password) {
        final int[] count = {0};
        final String[] userID = new String[1];

        try {
            String hashedPassword = Security.hashString(password);

            MongoCollection<Document> users = mongoDatabase.getCollection(USER_COLLECTION.getName());

            Bson accountFilter = Filters.eq("email", user);

            Bson passwordFilter = Filters.and(
                    Filters.eq("email", user),
                    Filters.eq("password", hashedPassword)
            );


            users.find(accountFilter).forEach((Block<Document>) (
                    document) -> {
                        count[0]++;
                    }
            );

            //If we dont find an account return immediately.We do this so we can handle login/create account on our native app
            if(count[0] == 0){
                return ResponseFormater.createResponse("No such account");
            }

            users.find(passwordFilter).forEach((Block<Document>) (
                    document) -> {
                        userID[0] = String.valueOf(document.get("id"));
                        count[0]++;
                    }
            );

            if(count[0] == 0){
                return ResponseFormater.createResponse("Password not valid");
            }

            return ResponseFormater.displayInformation(userID[0]);

        }catch (Exception e){
            return ResponseFormater.createResponse(e.getMessage());
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
            mongoDatabase.getCollection(CONTENT_COLLECTION.getName()).insertOne(document);

            return ResponseFormater.createResponse(null);
        } catch (Exception e) {
            return ResponseFormater.createResponse(e.getMessage());
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

}
