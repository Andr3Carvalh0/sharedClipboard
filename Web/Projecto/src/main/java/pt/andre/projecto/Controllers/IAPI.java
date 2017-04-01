package pt.andre.projecto.Controllers;

public interface IAPI {
    void push();
    void pull(String user);
    void createAccount();
    void authenticate(String account, String password);
}
