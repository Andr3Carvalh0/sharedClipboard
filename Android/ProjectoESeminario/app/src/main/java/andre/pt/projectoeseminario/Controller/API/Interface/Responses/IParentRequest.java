package andre.pt.projectoeseminario.Controller.API.Interface.Responses;


public interface IParentRequest {
    void showProgressDialog();
    void hideProgressDialog();
    void handleError(String title, String message);
}
