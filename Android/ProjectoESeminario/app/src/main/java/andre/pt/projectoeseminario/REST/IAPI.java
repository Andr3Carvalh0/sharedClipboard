package andre.pt.projectoeseminario.REST;


import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IAPI {

    String main = "http://projecto-testes.herokuapp.com/api/";
    String push = "push";
    String pull = "pull";
    String account = "account";

    @GET(account)
    boolean createAccount();

    @PUT(account)
    boolean authenticate(@Body("username") String username);


}
