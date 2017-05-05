package pt.andre.projecto.Controllers.URIs;

import java.util.Objects;


//Documentation: https://developers.google.com/cloud-messaging/http
public class FirebaseServer {

    private final String key;
    private final String google_server = "https://gcm-http.googleapis.com/gcm/send";

    public FirebaseServer(){
        key = System.getenv("FIREBASE_KEY");
        Objects.requireNonNull(key, "You must define a System Environment named FIREBASE_KEY with your Firebase key");
    }

    public boolean notify(String message, String... devices){
        return false;
    }
}
