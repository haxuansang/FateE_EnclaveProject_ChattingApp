package application.android.com.fatee.models.services.interfaces;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.ProfileResponse;

import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;

public interface ProfileService {
    void updateProfile(UserModel userModel, Callback<ProfileResponse> profileCallback);
}
