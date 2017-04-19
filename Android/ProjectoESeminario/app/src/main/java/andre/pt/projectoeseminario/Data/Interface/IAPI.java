package andre.pt.projectoeseminario.Data.Interface;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IAPI {

    String push = "push";
    String pull = "pull";
    String accountManagement = "account";

    @PUT(push)
    Call<ResponseBody> push(@Body String account);

    @GET(pull)
    Call<ResponseBody> pull(@Query("account") String account);

    @GET(accountManagement)
    Call<String> authenticate(@Query("account") String username, @Query("password") String password);

    @PUT(accountManagement)
    Call<ResponseBody> createAccount(@Query("account") String account, @Query("password") String password);

}
