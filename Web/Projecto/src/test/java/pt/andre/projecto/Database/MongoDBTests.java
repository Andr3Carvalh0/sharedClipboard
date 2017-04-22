package pt.andre.projecto.Database;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class MongoDBTests {

    private static MongoDB databaseConnector;
    private static String TEST_USER = "test";
    private static String TEST_PASSWORD = "test";

    @BeforeClass
    public static void setup(){
        databaseConnector = new MongoDB("localhost", "27017", "Testes");
        databaseConnector.createAccount(TEST_USER, TEST_PASSWORD);
    }

    @Test
    public void canConnectToDatabase(){
        String[] existingCollections = Iterables.toArray(databaseConnector.getMongoDatabase().listCollectionNames(), String.class);
        Assert.assertTrue(Arrays.stream(existingCollections).count() > 1);
    }

    @Test
    public void canDetectAccountCreationWithSameCredentials(){
        Assert.assertEquals(409, databaseConnector.createAccount(TEST_USER, TEST_PASSWORD).getResponseCode());
    }

    @Test
    public void canPushDataToDatabase(){
        throw new NotImplementedException();
    }

    @Test
    public void canPullDataFromDatabase(){
        throw new NotImplementedException();
    }

    @Test
    public void canAuthenticateUserFromDatabase(){
        System.out.println();
        DatabaseResponse authenticate = databaseConnector.authenticate(TEST_USER, TEST_PASSWORD);
        System.out.println();
        Assert.assertEquals(200, authenticate.getResponseCode());
        Assert.assertEquals("1", databaseConnector.authenticate(TEST_USER, TEST_PASSWORD).getResponseMessage());
    }

}
