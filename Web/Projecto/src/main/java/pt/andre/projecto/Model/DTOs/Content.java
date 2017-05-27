package pt.andre.projecto.Model.DTOs;

import com.mongodb.BasicDBObject;
import org.bson.Document;

import java.util.List;

public class Content {
    private long token;
    private String value;
    private boolean isMIME;
    private List<Document> mobileClients;

    public Content(long token, String content, boolean isMIME, List<Document> mobileClients) {
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

    public List<Document> getMobileClients() {
        return mobileClients;
    }


}
