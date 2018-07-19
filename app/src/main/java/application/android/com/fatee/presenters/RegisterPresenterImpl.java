package application.android.com.fatee.presenters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.RegisterResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.RegisterServiceImpl;
import application.android.com.fatee.models.services.interfaces.RegisterService;
import application.android.com.fatee.presenters.interfaces.RegisterPresenter;
import application.android.com.fatee.views.interfaces.RegisterView;

public class RegisterPresenterImpl implements RegisterPresenter{
    private RegisterService registerService;
    private RegisterView registerView;

    public RegisterPresenterImpl( RegisterView registerView) {
        registerService = new RegisterServiceImpl();
        this.registerView = registerView;
    }

    @Override
    public void createNewAccount(User user) {
        registerService.createNewAccount(user, new Callback<RegisterResponse>() {

            @Override
            public void onSuccess(RegisterResponse result) {
                registerView.notificationsAfterRegisteration(result);
            }

            @Override
            public void onError(Throwable what) {

            }
        });
    }
}
