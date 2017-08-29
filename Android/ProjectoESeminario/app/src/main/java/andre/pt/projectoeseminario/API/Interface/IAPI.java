package andre.pt.projectoeseminario.API.Interface;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IAPI {

    String push = "push";
    String accountManagement = "account";
    String deviceRegister = "registerDevice";

    @PUT(push)
    Call<ResponseBody> push(@Header("Authorization") String token, @Query("data") String data);

    @POST(accountManagement)
    Call<String> authenticate(@Header("Authorization") String token);

    @PUT(accountManagement)
    Call<String> createAccount(@Header("Authorization") String token);

    @PUT(deviceRegister)
    Call<ResponseBody> registerDevice(@Header("Authorization") String token, @Query("deviceIdentifier") String firebaseID, @Query("useSockets") boolean deviceType, @Query("deviceName") String deviceName);

}
