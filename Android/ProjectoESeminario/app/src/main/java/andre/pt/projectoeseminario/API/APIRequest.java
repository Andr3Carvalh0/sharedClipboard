package andre.pt.projectoeseminario.API;

import android.content.Context;
import android.os.Build;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import andre.pt.projectoeseminario.API.Interface.Responses.IResponse;
import andre.pt.projectoeseminario.API.Interface.IAPI;
import andre.pt.projectoeseminario.API.Interface.ProjectoAPI;
import andre.pt.projectoeseminario.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRequest {

    private IAPI mAPI;
    private IResponse iResponse;
    private Context ctx;
    private HashMap<Integer, Consumer<Integer>> respondeHandler;

    public APIRequest(IResponse resp, Context ctx){
        mAPI = ProjectoAPI.getAPI();
        iResponse = resp;
        this.ctx = ctx;
        this.respondeHandler = new HashMap<>();

        respondeHandler.put(200, (user) -> iResponse.handleSuccessfullyLogin(user));
        respondeHandler.put(400, (user) -> iResponse.handleNonExistingAccount());
        respondeHandler.put(403, (user) -> iResponse.handleError(getResourceString(R.string.Error403_Title), getResourceString(R.string.Error403_Message)));
        respondeHandler.put(409, (user) -> iResponse.handleError(getResourceString(R.string.Error409_Title), getResourceString(R.string.Error409_Message)));

    }

    public void registerDevice(long token, String firebase){
        mAPI.registerDevice(token, firebase, true, Build.MODEL).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    public void pushTextInformation(long token, String information){
        mAPI.push(token, information).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void handleAuthentication(String token){
        iResponse.showProgressDialog();

        mAPI.authenticate(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                iResponse.hideProgressDialog();

                if(response.body() != null){
                    handleHTTPResponse(response.code(), Integer.parseInt(response.body()));
                }else{
                    handleHTTPResponse(response.code(), -1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, 0);
            }
        });

    }

    public void handleAccountCreation(final String token){
        iResponse.showProgressDialog();

        mAPI.createAccount(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                iResponse.hideProgressDialog();

                if(response.body() != null){
                    handleHTTPResponse(response.code(), Integer.parseInt(response.body()));
                }else{
                    handleHTTPResponse(response.code(), -1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, 0);
            }
        });
    }

    /**
     * Handles what to do, on the HTTP response.
     */
    private void handleHTTPResponse(int code, int userID){
        if(respondeHandler.containsKey(code)) {
            respondeHandler.get(code).accept(userID);
            return;
        }

        iResponse.handleError(getResourceString(R.string.Error500_Title), getResourceString(R.string.Error500_Message));

    }
    
    private String getResourceString(int id){
        return ctx.getResources().getString(id);
    }

}
