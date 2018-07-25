package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface APIProfile {
    @PUT("/api/user")
    Call<ProfileResponse> profile(@Body UserModel userModel);
}
