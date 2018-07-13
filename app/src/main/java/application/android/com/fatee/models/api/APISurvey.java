package application.android.com.fatee.models.api;

import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APISurvey {
    @GET("/api/survey")
    Call<SurveyResponse> getSurvey();

    @POST("/api/survey/user")
    Call<SurveyResultResponse> getAnswers(@Body UserModel user);

    @POST("/api/survey")
    Call<SurveyResponseMessage> addAnswers(@Body AnswerRequest answerRequest);

    @PUT("/api/survey")
    Call<SurveyResponseMessage> editAnswers(@Body AnswerRequest answerRequest);

}
