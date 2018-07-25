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
                if (response.isSuccessful()) {
                    registercallback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
