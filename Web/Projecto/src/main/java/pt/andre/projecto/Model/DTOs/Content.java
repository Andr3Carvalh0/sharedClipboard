package pt.andre.projecto.Model.DTOs;

import com.mongodb.BasicDBObject;

import java.util.List;

/**
 * Created by Andre on 26/04/2017.
 */
public class Content {
    private long token;
    private String content;
    private List<BasicDBObject> androidClients;

    public Content(long token, String content, List<BasicDBObject> androidClients) {
        this.token = token;
        this.content = content;
        this.androidClients = androidClients;
    }

    public long getToken() {
        return token;
    }

    public String getContent() {
        return content;
    }

    public List<BasicDBObject> getAndroidClients() {
        return androidClients;
    }
}
