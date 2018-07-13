package application.android.com.fatee.helpers;

import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;

public class UserUtil {
    private static UserModel user;
    private static String surveyStatus;

    private static UserUtil instance;

    public static UserUtil getInstance(UserModel u, String s) {
        if(instance == null) {
            instance = new UserUtil();
            user = u;
            surveyStatus = s;
        }
        return instance;
    }

    public static UserModel getUser() {
        return user;
    }

    public static String getSurveyStatus() {
        return surveyStatus;
    }
}
