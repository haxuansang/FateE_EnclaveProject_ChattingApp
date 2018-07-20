package application.android.com.fatee.models.services.interfaces;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.RegisterResponse;
import application.android.com.fatee.models.entities.User;

public interface RegisterService {
    void createNewAccount(User user, Callback<RegisterResponse> registercallback);
}
