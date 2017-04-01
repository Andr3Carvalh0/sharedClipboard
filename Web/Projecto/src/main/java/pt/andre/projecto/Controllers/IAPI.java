package pt.andre.projecto.Controllers;

public interface IAPI {
    void push(String account);
    void pull(String user);
    void createAccount(String account, String password);
    void authenticate(String account, String password);
}
