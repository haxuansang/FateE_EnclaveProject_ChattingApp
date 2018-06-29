package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.SurveyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APISurvey {
    @GET("/api/survey")
    Call<SurveyResponse> getSurvey();
}
