package pt.andre.projecto.Model.Database;

import com.google.common.collect.Iterables;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import pt.andre.projecto.Model.Database.Utils.Collection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Implementation of the Database Interface using MongoDB
 */
public class MongoDB implements IDatabase {

    private final MongoDatabase mongoDatabase;
    private static final String DEFAULT_DB = "projecto";
    private static final Collection USER_COLLECTION = new Collection("users", new ValidationOptions().validator(Filters.exists("email")));
    private static final Collection CONTENT_COLLECTION = new Collection("content", new ValidationOptions());

    public MongoDB(String URL, String PORT){
        Objects.requireNonNull(URL, "The URL of the database cannot be null!");
        Objects.requireNonNull(URL, "The PORT of the database cannot be null!");

        MongoClientURI connectionString = new MongoClientURI("mongodb://" + URL + ":" + PORT);

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

    /**
     * Checks if the default collections are created.If not, delegates the creation of the same
     */
    private void initializeCollections(Collection... collections){
        String[] existingCollections = Iterables.toArray(getExistingCollections(), String.class);

        Arrays.stream(collections)
                .filter(collection -> checkIfCollectionAlreadyExists(collection.getName(), Arrays.stream(existingCollections)))
                .forEach(this::createCollection);

    }

    /**
     * Handles the creation of one collection @param collection
     */
    private void createCollection(Collection collection) {
        mongoDatabase.createCollection(collection.getName(), new CreateCollectionOptions().validationOptions(collection.getOptions()));
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
