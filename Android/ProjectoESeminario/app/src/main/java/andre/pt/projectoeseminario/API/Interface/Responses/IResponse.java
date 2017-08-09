package andre.pt.projectoeseminario.API.Interface.Responses;


public interface IResponse {
    void handleSuccessfullyLogin(String sub);
    void handleNonExistingAccount();
    void showProgressDialog();
    void hideProgressDialog();
    void handleError(String title, String message);
    void handlePush();
}
