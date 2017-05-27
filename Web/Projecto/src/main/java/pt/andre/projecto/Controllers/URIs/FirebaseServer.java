package pt.andre.projecto.Controllers.URIs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Objects;

import static java.lang.Thread.sleep;


//Documentation: https://developers.google.com/cloud-messaging/http
public class FirebaseServer {

    private final String key;
    private final String google_server = "https://gcm-http.googleapis.com/gcm/send";

    public FirebaseServer(){
        key = System.getenv("FIREBASE_KEY");
        Objects.requireNonNull(key, "You must define a System Environment named FIREBASE_KEY with your Firebase key");
    }

    public boolean notify(String message,boolean isMIME, String... devices){
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {

            for (int i = 0; i < devices.length; i++) {
                HttpPost request = new HttpPost(google_server);

                StringEntity params =new StringEntity(
                    "{" +
                        "data:{" +
                            "content:" + message +
                            "isMIME:" + isMIME +
                        "}," +
                        "to:{" +
                            "to:" + devices[i] +
                        "}" +
                    "}");
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Authorization", key);
                request.setEntity(params);

                HttpResponse response = httpClient.execute(request);

                sleep(1000);
                //handle response here...
            }
            return true;
        }catch (Exception ex) {
            System.out.println(ex);
            //handle exception here
            return false;
        }
    }
}
