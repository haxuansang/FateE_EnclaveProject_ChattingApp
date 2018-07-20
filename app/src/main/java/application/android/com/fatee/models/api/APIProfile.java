package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIProfile {
    @POST("/api/user")
    Call<ProfileResponse> profile(@Body UserModel userModel);
}
