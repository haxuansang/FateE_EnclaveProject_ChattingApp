package application.android.com.fatee.models.services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIAuth;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.RegisterResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.interfaces.RegisterService;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterServiceImpl implements RegisterService {

    @Override
    public void createNewAccount(User user, final Callback<RegisterResponse> registercallback) {
        APIAuth api = APIClient.getClient().create(APIAuth.class);
        Call<RegisterResponse> call = api.register(user);
        call.enqueue(new retrofit2.Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                System.out.println("Code:" + response.code());
                if (response.isSuccessful()) {
                    registercallback.onSuccess(response.body());
                }
//                if (response.code() == 400) {
//                    try {
//                        registercallback.onSuccess(createRegisterResponseFromErrorBory(response.errorBody().string()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

//    private RegisterResponse createRegisterResponseFromErrorBory(String errorBody) throws JSONException {
//        JSONObject jObjError = new JSONObject(errorBody);
//        RegisterResponse registerResponse = new RegisterResponse();
//        registerResponse.setResponseCode(jObjError.getString("responseCode"));
//        if(!"null".equals(resetPassLink) && !"null".equals(mail)) {
//            registerResponse.getLoginResponseInfo().setResetPassLink(jObjError.getJSONObject("loginResponseInfo").getString("resetPassLink"));
//            registerResponse.getLoginResponseInfo().setMail(jObjError.getJSONObject("loginResponseInfo").getString("mail"));
//        }
//        String message = jObjError.getJSONObject("loginResponseInfo").getString("message");
//        registerResponse.setMessage(message);
//        return registerResponse;
//    }
    }
