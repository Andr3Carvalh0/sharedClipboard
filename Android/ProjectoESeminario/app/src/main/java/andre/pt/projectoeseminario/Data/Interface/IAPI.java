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
    String androidRegister = "registerAndroid";

    @PUT(push)
    Call<ResponseBody> push(@Query("token") long account, @Query("data") String data);

    @GET(pull)
    Call<ResponseBody> pull(@Query("account") long account);

    @GET(accountManagement)
    Call<String> authenticate(@Query("account") String username, @Query("password") String password);

    @PUT(accountManagement)
    Call<String> createAccount(@Query("account") String account, @Query("password") String password);

    @PUT(androidRegister)
    Call<ResponseBody> registerAndroid(@Query("account") long account, @Query("deviceID") String firebaseID);

}
