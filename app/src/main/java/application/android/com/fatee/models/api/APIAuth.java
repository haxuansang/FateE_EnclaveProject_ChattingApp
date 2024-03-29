package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.RegisterResponse;
import application.android.com.fatee.models.entities.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIAuth {
    @POST("/api/login")
    Call<LoginResponse> login(@Body User user);

    @POST("/api/register")
    Call<RegisterResponse> register(@Body User user);
}
