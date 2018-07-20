package application.android.com.fatee.presenters.interfaces;

import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;

public interface ProfilePresenter {
    void updateProfile(UserModel userModel);
}
