package application.android.com.fatee.models.services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIAuth;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.interfaces.LoginService;
import retrofit2.Call;
import retrofit2.Response;

public class LoginServiceImpl implements LoginService{
    @Override
    public void getDataFromServer(User user, final Callback<LoginResponse> callback) {
        APIAuth api = APIClient.getClient().create(APIAuth.class);
        Call<LoginResponse> call = api.login(user);
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.code() == 200) {
                    callback.onSuccess(response.body());
                }
                if (response.code() == 400) {
                    try {
                        callback.onSuccess(createLoginResponseFromErrorBory(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private LoginResponse createLoginResponseFromErrorBory(String errorBody) throws JSONException {
        JSONObject jObjError = new JSONObject(errorBody);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResponseCode(jObjError.getString("responseCode"));
        loginResponse.setLoginResponseStatus(jObjError.getString("loginResponseStatus"));
        String resetPassLink = jObjError.getJSONObject("loginResponseInfo").getString("resetPassLink");
        String mail = jObjError.getJSONObject("loginResponseInfo").getString("mail");
        if(!"null".equals(resetPassLink) && !"null".equals(mail)) {
            loginResponse.getLoginResponseInfo().setResetPassLink(jObjError.getJSONObject("loginResponseInfo").getString("resetPassLink"));
            loginResponse.getLoginResponseInfo().setMail(jObjError.getJSONObject("loginResponseInfo").getString("mail"));
        }
        String message = jObjError.getJSONObject("loginResponseInfo").getString("message");
        loginResponse.getLoginResponseInfo().setMessage(message);
        return loginResponse;
    }
}
