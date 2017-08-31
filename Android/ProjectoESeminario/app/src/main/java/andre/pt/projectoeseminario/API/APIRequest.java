package andre.pt.projectoeseminario.API;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import java.util.HashMap;
import java.util.function.Consumer;
import andre.pt.projectoeseminario.API.Interface.Responses.IResponse;
import andre.pt.projectoeseminario.API.Interface.IAPI;
import andre.pt.projectoeseminario.API.Interface.ProjectoAPI;
import andre.pt.projectoeseminario.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles every request to the API
 */
public class APIRequest {

    private IAPI mAPI;
    private IResponse iResponse;
    private Context ctx;
    private HashMap<Integer, Consumer<String>> respondeHandler;

    public APIRequest(IResponse resp, Context ctx){
        mAPI = ProjectoAPI.getAPI();
        iResponse = resp;
        this.ctx = ctx;
        this.respondeHandler = new HashMap<>();

        respondeHandler.put(200, (user) -> iResponse.handleSuccessfullyLogin(user));
        respondeHandler.put(400, (user) -> iResponse.handleNonExistingAccount());
        respondeHandler.put(409, (user) -> iResponse.handleError(getResourceString(R.string.Error409_Title), getResourceString(R.string.Error409_Message)));

    }

    /**
     * Register device on the server
     * @param token the user id
     * @param firebase the firebase id
     */
    public void registerDevice(String token, String firebase, Runnable onError){
        mAPI.registerDevice(token, firebase, false, Build.MODEL).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() > 300)
                    onError.run();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onError.run();
            }
        });
    }

    /**
     * Push text to server.
     * @param token the user id
     * @param information the value to push
     */
    public void pushTextInformation(String token, String information){
        this.pushTextInformation(token, information, () -> {return;});

    }

    /**
     * Push text to server.
     * @param token the user id
     * @param information the value to push
     */
    public void pushTextInformation(String token, String information, Runnable runnable){
        mAPI.push(token, information).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() > 300)
                    Toast.makeText(ctx, ctx.getString(R.string.cannot_upload), Toast.LENGTH_SHORT).show();

                runnable.run();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, ctx.getString(R.string.cannot_upload), Toast.LENGTH_SHORT).show();
                runnable.run();
            }
        });

    }

    /**
     * Handles the login.
     * @param token the id token return by the google server
     */
    public void handleAuthentication(String token){
        iResponse.showProgressDialog();

        mAPI.authenticate(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                iResponse.hideProgressDialog();

                if(response.body() != null){
                    handleHTTPResponse(response.code(), response.body());
                }else{
                    handleHTTPResponse(response.code(), null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, null);
            }
        });

    }

    /**
     * Handles the account creation
     * @param token the token that came from the google server (ID Token)
     */
    public void handleAccountCreation(final String token){
        iResponse.showProgressDialog();

        mAPI.createAccount(token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                iResponse.hideProgressDialog();

                if(response.body() != null){
                    handleHTTPResponse(response.code(), response.body());
                }else{
                    handleHTTPResponse(response.code(), null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, null);
            }
        });
    }

    /**
     * Handles what to do, on the HTTP response.Used on the create/login method
     * @param code the code that the request returned
     * @param user the user id
     */
    private void handleHTTPResponse(int code, String user){
        if(respondeHandler.containsKey(code)) {
            respondeHandler.get(code).accept(user);
            return;
        }

        iResponse.handleError(getResourceString(R.string.Error500_Title), getResourceString(R.string.Error500_Message));

    }

    /**
     * Gets the string value of id
     * @param id the id of the resource
     * @return the id's value
     */
    private String getResourceString(int id){
        return ctx.getResources().getString(id);
    }

}
