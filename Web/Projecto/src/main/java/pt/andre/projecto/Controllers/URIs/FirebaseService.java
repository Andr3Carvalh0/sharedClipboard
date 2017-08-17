package pt.andre.projecto.Controllers.URIs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.andre.projecto.Controllers.Interfaces.INotify;


import java.util.Objects;

import static java.lang.Thread.sleep;

/*
* Class dedicated to handle the push of new information to the user mobile devices.
* Documentation: https://developers.google.com/cloud-messaging/http
* */
public class FirebaseService implements INotify{

    private final String key;
    private final String google_server = "https://gcm-http.googleapis.com/gcm/send";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Firebase ";

    public FirebaseService(){
        key = System.getenv("FIREBASE_KEY");
        Objects.requireNonNull(key, "You must define a System Environment named FIREBASE_KEY with your Firebase key");

        logger.info(TAG + "CTOR" );
    }

    /*
    * Makes a request to the Google servers for them to send our data to the @devices
    *
    * @param message: The content of the user that is stored on the database.This can be text or an URL to a file.
    * @param isMIME: Indicates if the message is a multimedia one, or not.
    * @param devices: The devices that will receive the message.
    *
    * @return if the operation ended without any exception
    * */
    public void notify(String sub, String messageJSON, String... devices){
        try {
            for (int i = 0; i < devices.length; i++) {

                HttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(google_server);
                post.setHeader("Content-type", "application/json");
                post.setHeader("Authorization", "key=" + key);

                JSONObject jsonOBJ = new JSONObject();
                jsonOBJ.put("to", devices[i]);

                jsonOBJ.put("data", messageJSON);

                post.setEntity(new StringEntity(jsonOBJ.toString(), "UTF-8"));
                HttpResponse response = client.execute(post);

                logger.info(TAG + "Firebase Response: " + response);
            }
        }catch (Exception ex) {
            logger.error(TAG + ex.getMessage());
        }
    }
}
