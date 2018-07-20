package application.android.com.fatee.models.services;

import android.util.Log;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIAuth;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.api.APIProfile;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.models.services.interfaces.ProfileService;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileServiceImpl implements ProfileService{

    @Override
    public void updateProfile(UserModel userModel, final Callback<ProfileResponse> profileCallback) {
        APIProfile api = APIClient.getClient().create(APIProfile.class);
        Call<ProfileResponse> call = api.profile(userModel);
        call.enqueue(new retrofit2.Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.code() == 200) {
                    profileCallback.onSuccess(response.body());
                }
//                if (response.code() == 400) {
//                    try {
//                        registercallback.onSuccess(createRegisterResponseFromErrorBory(response.errorBody().string()));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
    //    private RegisterResponse createRegisterResponseFromErrorBory(String errorBody) throws JSONException {
//        JSONObject jObjError = new JSONObject(errorBody);
//        RegisterResponse registerResponse = new RegisterResponse();
//        registerResponse.setResponseCode(jObjError.getString("responseCode"));
//        if(!"null".equals(resetPassLink) && !"null".equals(mail)) {
//            registerResponse.getLoginResponseInfo().setResetPassLink(jObjError.getJSONObject("loginResponseInfo").getString("resetPassLink"));
//            registerResponse.getLoginResponseInfo().setMail(jObjError.getJSONObject("loginResponseInfo").getString("mail"));
//        }
//        String message = jObjError.getJSONObject("loginResponseInfo").getString("message");
//        registerResponse.setMessage(message);
//        return registerResponse;
//    }
}
