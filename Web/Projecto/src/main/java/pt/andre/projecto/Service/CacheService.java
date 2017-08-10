package pt.andre.projecto.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.MapMaker;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import pt.andre.projecto.Service.Interfaces.ICacheService;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

//Note: Even though a cache is in use, this will not work in Heroku(at least in the free account)
// since the amount of ram given is minimal, so in heroku this solution has an addition cost of creating the cache every
// single time
public class CacheService implements ICacheService{

    private final String CLIENT_SECRET_ID = "74137919481-v4ldf8tl492o9saeaq6qe3gjnsvngs98.apps.googleusercontent.com";
    private final String Introspect_Endpoint = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
    private final HttpClient client = HttpClientBuilder.create().build();

    //Use Guava's implementation to get weak references goodies
    private ConcurrentMap<String, String> cache = new MapMaker()
            .weakKeys()
            .makeMap();

    private String handleGoogleAuthentication(String token){
        try{
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_SECRET_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            //On most clients the idToken will be != null. But we are unlucky, and the API thats developed for c# sucks, so the token provided will be null.
            //In that case we need to check ourselfs :(
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                return payload.getSubject();
            }else{
                HttpGet getRequest = new HttpGet(Introspect_Endpoint + token);

                String responseString = new BasicResponseHandler().handleResponse(client.execute(getRequest));
                JSONObject jsonObj = new JSONObject(responseString);

                return jsonObj.getString("sub");
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getUser(String token) {
        if(cache == null)
            cache = new MapMaker().weakKeys().makeMap();

        return cache.computeIfAbsent(token, this::handleGoogleAuthentication);
    }
}
