package application.android.com.fatee.utils;

import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;

public class UserUtil {
    private static UserModel userModel;

    private static User user;

    private static String surveyStatus;

    private static UserUtil instance;

    public static UserUtil getInstance(UserModel u, String s) {
        if(instance == null) {
            instance = new UserUtil();
        }
        userModel = u;
        surveyStatus = s;
        return instance;
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static String getSurveyStatus() {
        return surveyStatus;
    }

    public static void setSurveyStatus(String s) {
        surveyStatus = s;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserUtil.user = user;
    }
}
