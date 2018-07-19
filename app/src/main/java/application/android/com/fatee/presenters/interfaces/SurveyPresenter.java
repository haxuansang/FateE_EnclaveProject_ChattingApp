package application.android.com.fatee.presenters.interfaces;

import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.views.interfaces.OnReceiveSurveyListener;

public interface SurveyPresenter {
    void getSurvey();

    void getAnswers(SurveyResponse surveyResponse, UserModel user);

    void addAnswers(AnswerRequest answerRequest);

    void editAnswers(AnswerRequest answerRequest);

    void setListener(OnReceiveSurveyListener listener);

    OnReceiveSurveyListener getListener();
}