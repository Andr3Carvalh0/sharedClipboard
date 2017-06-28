package pt.andre.projecto.Model.DTOs;

import org.bson.Document;

import java.util.List;

/*
* Represents one User
* */
public class User {
    //The user id, that was given to him when he created the account.
    private final long id;

    //The user's email.
    private final String email;

    //The user's password.This password for security reasons its encrypted.
    private final String password;

    //List of the user's firebase ids.One id corresponds to a different mobile device
    private List<Document> mobileClients;

    //List of the user's firebase ids.One id corresponds to a different mobile device
    private List<Document> desktopClients;

    public User(long id, String email, String password, List<Document> mobileClients, List<Document> desktopClients) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.desktopClients = desktopClients;
        this.mobileClients = mobileClients;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Document> getMobileClients() {
        return mobileClients;
    }

    public List<Document> getDesktopClients() {
        return desktopClients;
    }
}
