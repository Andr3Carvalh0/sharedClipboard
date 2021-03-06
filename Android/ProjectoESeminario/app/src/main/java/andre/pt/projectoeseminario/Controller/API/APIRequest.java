package andre.pt.projectoeseminario.Controller.API;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import andre.pt.projectoeseminario.Controller.API.Interface.IAPI;
import andre.pt.projectoeseminario.Controller.API.Interface.ProjectoAPI;
import andre.pt.projectoeseminario.Controller.API.Interface.Responses.IAuthenticate;
import andre.pt.projectoeseminario.Controller.API.Interface.Responses.IParentRequest;
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
    private IParentRequest iResponse;
    private Context ctx;
    private HashMap<Integer, BiConsumer<String, Long>> respondeHandler;

    public APIRequest(Context ctx){
        this(null, ctx);
    }

    public APIRequest(IAuthenticate resp, Context ctx){
        this.mAPI = ProjectoAPI.getAPI();
        this.iResponse = resp;
        this.ctx = ctx;
        this.respondeHandler = new HashMap<>();

        respondeHandler.put(200, (user, order) -> ((IAuthenticate)iResponse).handleSuccessfullyLogin(user, order));
        respondeHandler.put(400, (user, nothing) -> ((IAuthenticate)iResponse).handleNonExistingAccount());
        respondeHandler.put(409, (user, nothing) -> iResponse.handleError(getResourceString(R.string.Error409_Title), getResourceString(R.string.Error409_Message)));
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
    public void pushTextInformation(String token, String information, String device){
        this.pushTextInformation(token, information, device, (s) -> {return;}, () -> {return;});

    }

    /**
     * Push text to server.
     * @param token the user id
     * @param information the value to push
     */
    public void pushTextInformation(String token, String information, String device, Consumer onSuccess, Runnable onFail){
        mAPI.push(token, information, device).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() > 300) {
                    Toast.makeText(ctx, ctx.getString(R.string.cannot_upload), Toast.LENGTH_SHORT).show();
                    onFail.run();
                }

                try {
                    String a = response.body();
                    JSONObject resp = new JSONObject(response.body());
                    JSONObject resp1 = new JSONObject(resp.get("data").toString());

                    onSuccess.accept(resp1.get("order").toString());
                } catch (JSONException e) {
                    onFail.run();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ctx, ctx.getString(R.string.cannot_upload), Toast.LENGTH_SHORT).show();
                onFail.run();
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
                    try {
                        JSONObject resp = new JSONObject(response.body());
                        String id = resp.getJSONObject("data").getString("id");
                        long order = resp.getJSONObject("data").getLong("order");

                        handleHTTPResponse(response.code(), id, order);

                    } catch (JSONException e) {
                        handleHTTPResponse(500, null, -1);
                    }

                }else{
                    handleHTTPResponse(response.code(), null, -1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, null, -1);
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
                    try {
                        JSONObject resp = new JSONObject(response.body());
                        String id = resp.getJSONObject("data").getString("id");
                        long order = resp.getJSONObject("data").getLong("order");

                        handleHTTPResponse(response.code(), id, order);

                    } catch (JSONException e) {
                        handleHTTPResponse(500, null, -1);
                    }
                }else{
                    handleHTTPResponse(response.code(), null, -1);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                iResponse.hideProgressDialog();
                handleHTTPResponse(500, null, -1);
            }
        });
    }

    /**
     * Handles what to do, on the HTTP response.Used on the create/login method
     * @param code the code that the request returned
     * @param user the user id
     */
    private void handleHTTPResponse(int code, String user, long order){
        if(respondeHandler.containsKey(code)) {
            respondeHandler.get(code).accept(user, order);
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
