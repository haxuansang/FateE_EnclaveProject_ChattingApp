package application.android.com.fatee.views.interfaces;

import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;

public interface SurveyView {
    void viewSurvey(SurveyResponse surveyResponse);

    void viewSurveyResult(SurveyResponse surveyResponse, SurveyResultResponse surveyResultResponse);

    void viewNotificationAfterAddSurvey(SurveyResponseMessage surveyResponseMessage);

    void viewNotificationAfterEditSurvey(SurveyResponseMessage surveyResponseMessage);

}
