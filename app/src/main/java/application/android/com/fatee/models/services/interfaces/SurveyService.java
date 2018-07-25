package application.android.com.fatee.models.services.interfaces;


import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.UserModel;

public interface SurveyService {
    void getSurvey(Callback<SurveyResponse> callback);

    void getAnswers(UserModel user, Callback<SurveyResultResponse> callback);

    void addAnswers(AnswerRequest answerRequest, Callback<SurveyResponseMessage> callback);


    void editAnswers(AnswerRequest answerRequest, Callback<SurveyResponseMessage> callback);
}
