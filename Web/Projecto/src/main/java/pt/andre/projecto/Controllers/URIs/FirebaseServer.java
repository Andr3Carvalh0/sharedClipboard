package pt.andre.projecto.Controllers.URIs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;


import java.util.Objects;

import static java.lang.Thread.sleep;

/*
* Class dedicated to handle the push of new information to the user mobile devices.
* Documentation: https://developers.google.com/cloud-messaging/http
* */
public class FirebaseServer {

    private final String key;
    private final String google_server = "https://gcm-http.googleapis.com/gcm/send";

    public FirebaseServer(){
        key = System.getenv("FIREBASE_KEY");
        Objects.requireNonNull(key, "You must define a System Environment named FIREBASE_KEY with your Firebase key");
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
    public boolean notify(String message, boolean isMIME, String... devices){
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            for (int i = 0; i < devices.length; i++) {

                HttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(google_server);
                post.setHeader("Content-type", "application/json");
                post.setHeader("Authorization", "key=" + key);

                JSONObject jsonOBJ = new JSONObject();
                jsonOBJ.put("to", devices[i]);

                JSONObject data = new JSONObject();
                data.put("content", message);
                data.put("isMIME", isMIME);

                jsonOBJ.put("data", data);

                post.setEntity(new StringEntity(jsonOBJ.toString(), "UTF-8"));
                HttpResponse response = client.execute(post);
                System.out.println(response);

                sleep(5000);
            }
            return true;
        }catch (Exception ex) {
            System.out.println(ex);
            //handle exception here
            return false;
        }
    }
}
