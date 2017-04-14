package andre.pt.projectoeseminario.Activities;

public interface IResponse {

    void handleSuccessfullyLogin();
    void handleNonExistingAccount();
    void handleError(String title, String message);
    void displayWaitingDialog();
    void hideWaitingDialog();

}
