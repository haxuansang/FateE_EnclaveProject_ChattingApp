package application.android.com.fatee.models.services.interfaces;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.User;

public interface LoginService {
    void getDataFromServer(User user, Callback<LoginResponse> callback);
}
