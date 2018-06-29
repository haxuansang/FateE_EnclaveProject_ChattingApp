package application.android.com.fatee.models.services.interfaces;


import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.SurveyResponse;

public interface SurveyService {
    void getSurvey(Callback<SurveyResponse> callback);
}
