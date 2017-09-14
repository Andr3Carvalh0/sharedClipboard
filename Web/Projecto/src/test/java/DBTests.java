import com.google.common.collect.Iterables;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.andre.projecto.Model.DTOs.Wrappers.DeviceWrapper;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Output.Interfaces.DatabaseResponse;

import java.util.Arrays;
import java.util.List;

public class DBTests {

    private static MongoDB databaseConnector;
    private static String TEST_USER = "1";
    private static String HOST = System.getenv("MONGO_HOST_TEST");
    private static String PORT = System.getenv("MONGO_PORT_TEST");
    private static String DB = System.getenv("MONGO_DATABASE_TEST");
    private static String USER = System.getenv("MONGO_USER_TEST");
    private static String PASS = System.getenv("MONGO_PASS_TEST");

    @BeforeClass
    public static void preSetup() {
        databaseConnector = new MongoDB(HOST, PORT, DB, USER, PASS);
        databaseConnector.createAccount(TEST_USER);
    }

    @Before
    public void setup() {
        databaseConnector.push(TEST_USER, "Ola", false, 0);

    }

    @Test
    public void canConnectToDatabase() {
        String[] existingCollections = Iterables.toArray(databaseConnector.getMongoDatabase().listCollectionNames(), String.class);
        Assert.assertTrue(Arrays.stream(existingCollections).count() > 1);
    }

    @Test
    public void canDetectAccountCreationWithSameCredentials() {
        Assert.assertEquals(409, databaseConnector.createAccount(TEST_USER).getResponseCode());
    }

    @Test
    public void canPushDataToDatabase() {
        databaseConnector.push(TEST_USER, "Adeus", false, 0);
        JSONObject js = new JSONObject((String)databaseConnector.pull(TEST_USER).getResponseMessage());


        Assert.assertEquals("Adeus", js.getString("content"));
    }

    @Test
    public void canPullDataFromDatabase() {
        JSONObject json = new JSONObject((String)databaseConnector.pull(TEST_USER).getResponseMessage());
        Assert.assertEquals("Ola", json.getString("content"));

    }

    @Test
    public void canAuthenticateUserFromDatabase() {
        DatabaseResponse authenticate = databaseConnector.authenticate(TEST_USER);
        Assert.assertEquals(200, authenticate.getResponseCode());
        JSONObject js = new JSONObject((String)databaseConnector.authenticate(TEST_USER).getResponseMessage());


        Assert.assertEquals("1", js.getJSONObject("data").getString("id"));
    }

    @Test
    public void canRegisterMobileDevice() {
        databaseConnector.registerMobileDevice(TEST_USER, "TEST", "iPhonen");
        List<DeviceWrapper> allDevices = databaseConnector.getMobileDevices(TEST_USER);

        for (DeviceWrapper device : allDevices) {
            if (device.getId().equals("TEST")) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.assertTrue(false);
    }

    @Test
    public void canReg1isterMobileDevice() {
        databaseConnector.registerDesktopDevice(TEST_USER, "TEST", "MAC");

        List<DeviceWrapper> allDevices = databaseConnector.getDesktopDevices(TEST_USER);

        for (DeviceWrapper device : allDevices) {
            if (device.getId().equals("TEST")) {
                Assert.assertTrue(true);
                return;
            }
        }

        Assert.assertTrue(false);
    }
}