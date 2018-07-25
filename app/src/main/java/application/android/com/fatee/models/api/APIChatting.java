package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.QuickBloxResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIChatting {
    @GET("/api/quickblox")
    Call<QuickBloxResponse> getQuickBloxFromServer();
}
