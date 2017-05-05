package pt.andre.projecto.Model.DTOs;

import com.mongodb.BasicDBObject;

import java.util.List;

public class Content {
    private long token;
    private String value;
    private boolean isMIME;
    private List<BasicDBObject> androidClients;

    public Content(long token, String content, boolean isMIME, List<BasicDBObject> androidClients) {
        this.token = token;
        this.value = content;
        this.isMIME = isMIME;
        this.androidClients = androidClients;
    }

    public boolean isMIME() {
        return isMIME;
    }

    public long getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    public List<BasicDBObject> getAndroidClients() {
        return androidClients;
    }
}
