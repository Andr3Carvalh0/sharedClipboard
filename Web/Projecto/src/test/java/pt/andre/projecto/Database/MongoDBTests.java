package pt.andre.projecto.Database;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.andre.projecto.Model.Database.IDatabase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class MongoDBTests {

    private static IDatabase databaseConnector;

    @BeforeClass
    public static void setup(){
        databaseConnector = new IDatabase("localhost:27017");

    }

    @Test
    public void canConnectToDatabase(){
        String[] existingCollections = Iterables.toArray(databaseConnector.getMongoDatabase().listCollectionNames(), String.class);
        Assert.assertTrue(Arrays.stream(existingCollections).count() > 1);
    }

    @Test
    public void canPushDataToDatabase(){
        throw new NotImplementedException();
    }

    @Test
    public void canPullDataFromDatabase(){
        throw new NotImplementedException();
    }

}
