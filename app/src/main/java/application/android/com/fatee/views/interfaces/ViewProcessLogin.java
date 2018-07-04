package application.android.com.fatee.views.interfaces;

import application.android.com.fatee.models.entities.LoginResponse;

public interface ViewProcessLogin {
    void noticeForUserAfterLogin(LoginResponse userResponse);
    void caseLostNetworkConnection();
    void caseHaveNetworkConnection();
}
