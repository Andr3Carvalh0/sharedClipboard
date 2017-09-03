package andre.pt.projectoeseminario.Controller.API.Interface.Responses;


public interface IAuthenticate extends IParentRequest {
    void handleSuccessfullyLogin(String sub);
    void handleNonExistingAccount();
}
