package application.android.com.fatee.presenters;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.LoginServiceImpl;
import application.android.com.fatee.models.services.interfaces.LoginService;
import application.android.com.fatee.presenters.interfaces.PresenterProcessLogin;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class ProcessLogicPresenter implements PresenterProcessLogin {
    private ViewProcessLogin viewProcessLogin;
    private LoginService loginService;


    public ProcessLogicPresenter(ViewProcessLogin viewProcessLogin) {
        this.viewProcessLogin = viewProcessLogin;
        loginService = new LoginServiceImpl();
    }


    @Override
    public void getDataFromServer(User user) {
        loginService.getDataFromServer(user, new Callback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
                viewProcessLogin.noticeForUserAfterLogin(result);
            }

            @Override
            public void onError(Throwable what) {
            }
        });
    }

    @Override
    public void caseLostConnection() {
        viewProcessLogin.caseLostNetworkConnection();
    }

    @Override
    public void caseHaveConnection() {
        viewProcessLogin.caseHaveNetworkConnection();
    }
}
