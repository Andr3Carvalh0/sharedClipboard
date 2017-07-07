package andre.pt.projectoeseminario.API.Interface;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IAPI {

    String push = "push";
    String accountManagement = "account";
    String deviceRegister = "registerDevice";

    @PUT(push)
    Call<ResponseBody> push(@Query("token") long account, @Query("data") String data);

    @POST(accountManagement)
    Call<String> authenticate(@Query("token")String token);

    @POST(accountManagement)
    Call<String> createAccount(@Query("token")String token);

    @PUT(deviceRegister)
    Call<ResponseBody> registerDevice(@Query("account") long account, @Query("deviceIdentifier") String firebaseID, @Query("deviceType") boolean deviceType, @Query("deviceName") String deviceName);

}
