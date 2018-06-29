package application.android.com.fatee.models.services;

import java.util.List;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.api.APISurvey;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.services.interfaces.SurveyService;
import retrofit2.Call;
import retrofit2.Response;

public class SurveyServiceImpl implements SurveyService{
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
}
