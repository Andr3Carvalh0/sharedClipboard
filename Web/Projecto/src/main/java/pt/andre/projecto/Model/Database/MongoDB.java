package pt.andre.projecto.Model.Database;

import com.google.common.collect.Iterables;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


//TODO: Read this https://www.mongodb.com/blog/post/password-authentication-with-mongoose-part-1
/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase {

    private final MongoDatabase mongoDatabase;
    private static final String DEFAULT_DB = "projecto";
    private static final String USER_COLLECTION = "users";
    private static final String CONTENT_COLLECTION = "content";

    public MongoDB(String URL, String PORT){
        Objects.requireNonNull(URL, "The URL of the database cannot be null!");
        Objects.requireNonNull(URL, "The PORT of the database cannot be null!");

        MongoClientURI connectionString = new MongoClientURI("mongodb://" + URL + ":" + PORT);

        MongoClient mongoClient = new MongoClient(connectionString);
        mongoDatabase = mongoClient.getDatabase(DEFAULT_DB);

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
    public DatabaseResponse authenticate(String user, String pass) {
        throw new NotImplementedException();
    }

    @Override
    public DatabaseResponse createAccount(String user, String pass) {
        int responseCode = 200;
        String responseMessage = "Success!";

        //TODO:Remove this line from here!Its only here to test something
        mongoDatabase.getCollection(USER_COLLECTION).createIndex(new Document("email", 1), new IndexOptions().unique(true));


        Document document = new Document();
        document.put("index", mongoDatabase.getCollection(USER_COLLECTION).count() + 1);
        document.put("email", user);
        document.put("password", pass);

        try {
            mongoDatabase.getCollection(USER_COLLECTION).insertOne(document);
        }catch (Exception e){
            responseCode = 406;
            responseMessage = e.getMessage();
        }

        return new DatabaseResponse(responseCode, responseMessage);
    }

    /**
     * Checks if the default collections are created.If not, delegates the creation of the same
     */
    private void initializeCollections(String... collections){
        String[] existingCollections = Iterables.toArray(getExistingCollections(), String.class);

        Arrays.stream(collections)
                .filter(collection -> checkIfCollectionAlreadyExists(collection, Arrays.stream(existingCollections)))
                .forEach(this::createCollection);

    }

    /**
     * Handles the creation of one collection @param collection
     */
    private void createCollection(String collection) {
        mongoDatabase.createCollection(collection);

    }

    /**
     * Checks if the collection named @param collection exists.
     * @param existingCollections the existing collections
     */
    private boolean checkIfCollectionAlreadyExists(String collection, Stream<String> existingCollections) {
        return existingCollections
                .filter(collection::equals)
                .count() == 0;
    }

    private Iterable<String> getExistingCollections(){
        return mongoDatabase.listCollectionNames();
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

}
