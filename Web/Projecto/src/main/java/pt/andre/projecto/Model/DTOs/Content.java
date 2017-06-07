package pt.andre.projecto.Model.DTOs;

import org.bson.Document;

import java.util.List;

/*
* Represents one Item of the Content Table
* */
public class Content {
    // The user token
    private long token;

    //The stored value.It can be a textual data or a URL to a resource
    private String value;

    //Indicates whether id a URL to a resource, or the resource itself
    private boolean isMIME;

    //List of the user's firebase ids.One id corresponds to a different mobile device
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
