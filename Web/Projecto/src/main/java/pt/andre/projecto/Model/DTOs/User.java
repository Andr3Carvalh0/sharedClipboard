package pt.andre.projecto.Model.DTOs;

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

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
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
}
