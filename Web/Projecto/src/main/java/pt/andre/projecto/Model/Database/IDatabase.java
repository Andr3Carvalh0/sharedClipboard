package pt.andre.projecto.Model.Database;

public interface IDatabase {

    void push(String user, String data);
    void pull(String user);
    void authenticate(String user, String pass);
    boolean createUser(String user, String pass);
}
