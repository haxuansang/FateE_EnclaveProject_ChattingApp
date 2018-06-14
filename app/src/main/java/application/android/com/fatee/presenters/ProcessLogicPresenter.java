package application.android.com.fatee.presenters;

import application.android.com.fatee.presenters.interfaces.PresenterProcessLogin;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class ProcessLogicPresenter implements PresenterProcessLogin {


    ViewProcessLogin viewProcessLogin;

    public ProcessLogicPresenter(ViewProcessLogin viewProcessLogin) {
        this.viewProcessLogin = viewProcessLogin;
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
