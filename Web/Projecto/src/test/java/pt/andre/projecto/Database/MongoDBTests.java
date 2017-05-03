package pt.andre.projecto.Database;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import java.util.Arrays;

public class MongoDBTests {

    private static MongoDB databaseConnector;
    private static String TEST_USER = "test";
    private static String TEST_PASSWORD = "test";

    @BeforeClass
    public static void preSetup(){
        databaseConnector = new MongoDB("localhost", "27017", "Testes");
        databaseConnector.createAccount(TEST_USER, TEST_PASSWORD);
    }

    @Before
    public void setup(){
        databaseConnector.push(1, "Ola");

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
        databaseConnector.push(1, "Adeus");

        Assert.assertEquals("Adeus", databaseConnector.pull(1).getResponseMessage());
    }

    @Test
    public void canPullDataFromDatabase(){
        Assert.assertEquals("Ola", databaseConnector.pull(1).getResponseMessage());

    }

    @Test
    public void canAuthenticateUserFromDatabase(){
        DatabaseResponse authenticate = databaseConnector.authenticate(TEST_USER, TEST_PASSWORD);
        Assert.assertEquals(200, authenticate.getResponseCode());
        Assert.assertEquals("1", databaseConnector.authenticate(TEST_USER, TEST_PASSWORD).getResponseMessage());
    }

    @Test
    public void cdsase(){
        databaseConnector.registerAndroidDevice(1, "ABC");
    }
}
