package application.android.com.fatee.presenters;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.models.services.SurveyServiceImpl;
import application.android.com.fatee.models.services.interfaces.SurveyService;
import application.android.com.fatee.presenters.interfaces.SurveyPresenter;
import application.android.com.fatee.views.interfaces.OnReceiveSurveyListener;
import application.android.com.fatee.views.interfaces.SurveyView;

public class SurveyPresenterImpl implements SurveyPresenter{
    private SurveyService surveyService;
    private SurveyView surveyView;
    private OnReceiveSurveyListener listener;

    public SurveyPresenterImpl(SurveyView surveyView) {
        surveyService = new SurveyServiceImpl();
        this.surveyView = surveyView;
    }

    @Override
    public void getSurvey() {
        surveyService.getSurvey(new Callback<SurveyResponse>() {
            @Override
            public void onSuccess(SurveyResponse result) {
                surveyView.viewSurvey(result);
            }

            @Override
            public void onError(Throwable what) {
            }
        });
    }

    @Override
    public void getAnswers(final SurveyResponse surveyResponse, final UserModel user) {
        surveyService.getAnswers(user, new Callback<SurveyResultResponse>() {
            @Override
            public void onSuccess(SurveyResultResponse result) {
                surveyView.viewSurveyResult(surveyResponse, result);
            }

            @Override
            public void onError(Throwable what) {}
        });
    }

    @Override
    public void addAnswers(AnswerRequest answerRequest) {
       surveyService.addAnswers(answerRequest, new Callback<SurveyResponseMessage>() {
           @Override
           public void onSuccess(SurveyResponseMessage result) {
                surveyView.viewNotificationAfterAddSurvey(result);
           }

           @Override
           public void onError(Throwable what) {

           }
       });
    }

    @Override
    public void editAnswers(AnswerRequest answerRequest) {
        surveyService.editAnswers(answerRequest, new Callback<SurveyResponseMessage>() {
            @Override
            public void onSuccess(SurveyResponseMessage result) {
                surveyView.viewNotificationAfterEditSurvey(result);
            }

            @Override
            public void onError(Throwable what) {

            }
        });
    }

    @Override
    public void setListener(OnReceiveSurveyListener listener) {
        this.listener = listener;
    }

    @Override
    public OnReceiveSurveyListener getListener() {
        return listener;
    }
}
