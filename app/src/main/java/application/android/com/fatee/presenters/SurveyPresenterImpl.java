package application.android.com.fatee.presenters;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.services.SurveyServiceImpl;
import application.android.com.fatee.models.services.interfaces.SurveyService;
import application.android.com.fatee.presenters.interfaces.SurveyPresenter;
import application.android.com.fatee.views.interfaces.SurveyView;

public class SurveyPresenterImpl implements SurveyPresenter{
    private SurveyService surveyService;
    private SurveyView surveyView;

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
}
