package pt.andre.projecto.Model.DTOs;

import com.mongodb.BasicDBObject;

import java.util.List;

public class Content {
    private long token;
    private String value;
    private boolean isMIME;
    private List<BasicDBObject> mobileClients;

    public Content(long token, String content, boolean isMIME, List<BasicDBObject> mobileClients) {
        this.token = token;
        this.value = content;
        this.isMIME = isMIME;
        this.mobileClients = mobileClients;
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

    public List<BasicDBObject> getMobileClients() {
        return mobileClients;
    }


}
