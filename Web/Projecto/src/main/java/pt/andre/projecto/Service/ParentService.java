package pt.andre.projecto.Service;


import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class ParentService {

    private final URL CLIENT_SECRET_FILE = ClassLoader.getSystemResource("config.json");

    protected String handleGoogleAuthentication(String token) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new FileReader(CLIENT_SECRET_FILE.getPath()));


        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientSecrets.getDetails().getClientId()))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                return payload.getSubject();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

}
