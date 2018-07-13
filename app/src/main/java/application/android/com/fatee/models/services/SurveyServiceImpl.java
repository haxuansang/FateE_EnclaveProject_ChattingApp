package application.android.com.fatee.models.services;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.api.APISurvey;
import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.AnswerResponse;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.models.services.interfaces.SurveyService;
import retrofit2.Call;
import retrofit2.Response;

public class SurveyServiceImpl implements SurveyService {
    @Override
    public void getSurvey(final Callback<SurveyResponse> callback) {
        APISurvey apiSurvey = APIClient.getClient().create(APISurvey.class);
        Call<SurveyResponse> call = apiSurvey.getSurvey();
        call.enqueue(new retrofit2.Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void getAnswers(UserModel user, final Callback<SurveyResultResponse> callback) {
        APISurvey apiSurvey = APIClient.getClient().create(APISurvey.class);
        Call<SurveyResultResponse> call = apiSurvey.getAnswers(user);
        call.enqueue(new retrofit2.Callback<SurveyResultResponse>() {
            @Override
            public void onResponse(Call<SurveyResultResponse> call, Response<SurveyResultResponse> response) {
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<SurveyResultResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void addAnswers(AnswerRequest answerRequest, final Callback<SurveyResponseMessage> callback) {
        APISurvey apiSurvey = APIClient.getClient().create(APISurvey.class);
        Call<SurveyResponseMessage> call = apiSurvey.addAnswers(answerRequest);
        call.enqueue(new retrofit2.Callback<SurveyResponseMessage>() {
            @Override
            public void onResponse(Call<SurveyResponseMessage> call, Response<SurveyResponseMessage> response) {
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<SurveyResponseMessage> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void editAnswers(AnswerRequest answerRequest, final Callback<SurveyResponseMessage> callback) {
        APISurvey apiSurvey = APIClient.getClient().create(APISurvey.class);
        Call<SurveyResponseMessage> call = apiSurvey.editAnswers(answerRequest);
        call.enqueue(new retrofit2.Callback<SurveyResponseMessage>() {
            @Override
            public void onResponse(Call<SurveyResponseMessage> call, Response<SurveyResponseMessage> response) {
                if (response.code() == 200) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<SurveyResponseMessage> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
