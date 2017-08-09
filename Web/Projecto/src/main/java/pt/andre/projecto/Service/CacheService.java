package pt.andre.projecto.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.MapMaker;
import pt.andre.projecto.Service.Interfaces.ICacheService;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

public class CacheService implements ICacheService{

    private final URL CLIENT_SECRET_FILE = ClassLoader.getSystemResource("config.json");

    //Use Guava's implementation to get weak references goodies
    private ConcurrentMap<String, String> cache = new MapMaker().weakKeys().makeMap();

    private String handleGoogleAuthentication(String token){

        try{
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE.getPath()));


            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientSecrets.getDetails().getClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                return payload.getSubject();
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getUser(String token) {
        return cache.computeIfAbsent(token, this::handleGoogleAuthentication);
    }
}
