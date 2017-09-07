package pt.andre.projecto.Model.DTOs;

import org.bson.Document;

import java.util.List;

/*
* Represents one User
* */
public class User {
    //The user id, that was given to him when he created the account.
    private final String id;

    //List of the user's firebase ids.One id corresponds to a different mobile device
    private List<Document> mobileClients;

    //List of the user's firebase ids.One id corresponds to a different mobile device
    private List<Document> desktopClients;

    //order of request
    private int order;

    public User(String id, List<Document> mobileClients, List<Document> desktopClients, int order) {
        this.id = id;
        this.desktopClients = desktopClients;
        this.mobileClients = mobileClients;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public List<Document> getMobileClients() {
        return mobileClients;
    }

    public List<Document> getDesktopClients() {
        return desktopClients;
    }

    public int getOrder(){
        return order;
    }
}
