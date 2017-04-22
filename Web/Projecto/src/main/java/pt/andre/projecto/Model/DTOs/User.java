package pt.andre.projecto.Model.DTOs;

/**
 * Created by Andre on 22/04/2017.
 */
public class User {
    private final long id;
    private final String email;
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
