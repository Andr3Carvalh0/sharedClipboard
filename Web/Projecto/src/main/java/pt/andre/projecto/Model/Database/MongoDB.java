package pt.andre.projecto.Model.Database;


import com.google.common.collect.Iterables;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import org.springframework.stereotype.Component;
import pt.andre.projecto.Model.Database.Utils.Collection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Implementation of the Database Interface using MongoDB
 */
@Component
public class MongoDB extends ParentDatabase {

    private final MongoDatabase mongoDatabase;

    private static final String DEFAULT_DB = "projecto";

    private static final Collection USER_COLLECTION = new Collection("users", new ValidationOptions().validator(Filters.exists("email")));
    private static final Collection CONTENT_COLLECTION = new Collection("content", new ValidationOptions().validator(Filters.exists("email")));

    /*
    * Automatically called by the SpringFramework.
    * */
    public MongoDB(){
        this(System.getenv("MONGO_URL"));
    }

    /*
    *  Only use this contructor for tests.You should always use the parameterless ctor.
    * */
    public MongoDB(String URL){
        Objects.requireNonNull(URL, "You must have the 'MONGO_URL' environment variable set.This variable must be in the following format: [URI]:[PORT]");

        MongoClientURI connectionString = new MongoClientURI("mongodb://" + System.getenv("MONGO_URL"));

        MongoClient mongoClient = new MongoClient(connectionString);
        mongoDatabase = mongoClient.getDatabase(DEFAULT_DB);

        initializeCollections(USER_COLLECTION, CONTENT_COLLECTION);

    }

    @Override
    public void push(String user, String data) {
        throw new NotImplementedException();
    }

    @Override
    public void pull(String user) {
        throw new NotImplementedException();
    }

    @Override
    public void authenticate(String user, String pass) {
        throw new NotImplementedException();
    }

    @Override
    public boolean createUser(String user, String pass) {
        throw new NotImplementedException();
    }

    private void initializeCollections(Collection... collections){
        String[] existingCollections = Iterables.toArray(getExistingCollections(), String.class);

        Arrays.stream(collections)
                .filter(collection -> checkIfCollectionAlreadyExists(collection.getName(), Arrays.stream(existingCollections)))
                .forEach(this::createCollection);

    }

    private void createCollection(Collection collection) {
        mongoDatabase.createCollection(collection.getName(), new CreateCollectionOptions().validationOptions(collection.getOptions()));
    }

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
