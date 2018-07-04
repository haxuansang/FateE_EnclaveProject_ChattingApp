package application.android.com.fatee.presenters.interfaces;

import application.android.com.fatee.models.entities.User;

public interface PresenterProcessLogin {
    void getDataFromServer(User user);
    void caseLostConnection();
    void caseHaveConnection();
}
