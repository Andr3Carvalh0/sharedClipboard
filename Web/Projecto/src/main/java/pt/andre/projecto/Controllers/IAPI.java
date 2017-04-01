package pt.andre.projecto.Controllers;

public interface IAPI {
    String push(String account);
    String pull(String user);
    String createAccount(String account, String password);
    String authenticate(String account, String password);
}
