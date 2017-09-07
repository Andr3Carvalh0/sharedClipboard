package pt.andre.projecto.Model.DTOs;

import org.bson.Document;

import java.util.List;

/*
* Represents one Item of the Content Table
* */
public class Content {
    // The user token
    private String token;

    //The stored value.It can be a textual data or a URL to a resource
    private String value;

    //last order number
    private int order;

    //Indicates whether id a URL to a resource, or the resource itself
    private boolean isMIME;

    public Content(String token, String content, boolean isMIME, int order) {
        this.token = token;
        this.value = content;
        this.isMIME = isMIME;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public boolean isMIME() {
        return isMIME;
    }

    public String getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

}
