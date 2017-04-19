package andre.pt.projectoeseminario.Data.Interface;

public interface IResponse {

    void handleSuccessfullyLogin(int user);
    void handleNonExistingAccount();
    void handleError(String title, String message);
    void showProgressDialog();
    void hideProgressDialog();

}
