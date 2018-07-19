package application.android.com.fatee.presenters;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.models.services.ProfileServiceImpl;
import application.android.com.fatee.models.services.RegisterServiceImpl;
import application.android.com.fatee.models.services.interfaces.ProfileService;
import application.android.com.fatee.models.services.interfaces.RegisterService;
import application.android.com.fatee.presenters.interfaces.ProfilePresenter;
import application.android.com.fatee.views.interfaces.ProfileView;
import application.android.com.fatee.views.interfaces.RegisterView;

public class ProfilePresenterImpl implements ProfilePresenter{
    private ProfileService profileService;
    private ProfileView profileView;

    public ProfilePresenterImpl( ProfileView profileView) {
        profileService = new ProfileServiceImpl();
        this.profileView = profileView;
    }
    @Override
    public void updateProfile(UserModel userModel) {
        profileService.updateProfile(userModel, new Callback<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse result) {
                profileView.notificationsAfterUpdate(result);
            }

            @Override
            public void onError(Throwable what) {

            }
        });
    }
}
