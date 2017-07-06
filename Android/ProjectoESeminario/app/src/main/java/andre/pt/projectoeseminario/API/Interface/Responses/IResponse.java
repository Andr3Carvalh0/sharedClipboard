package andre.pt.projectoeseminario.API.Interface.Responses;


public interface IResponse {
    void handleSuccessfullyLogin(int user);
    void handleNonExistingAccount();
    void showProgressDialog();
    void hideProgressDialog();
    void handleError(String title, String message);
    void handlePull(String msg);
    void handlePush();
}
