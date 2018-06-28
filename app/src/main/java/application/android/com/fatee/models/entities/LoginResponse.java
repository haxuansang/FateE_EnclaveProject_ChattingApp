package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("loginResponseStatus")
    @Expose
    private String loginResponseStatus;

    @SerializedName("loginResponseInfo")
    @Expose
    private LoginResponseInfo loginResponseInfo;
    @SerializedName("user")
    @Expose
    private UserModel userModel;

    public LoginResponse() {
        loginResponseInfo = new LoginResponseInfo();
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getLoginResponseStatus() {
        return loginResponseStatus;
    }

    public void setLoginResponseStatus(String loginResponseStatus) {
        this.loginResponseStatus = loginResponseStatus;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public LoginResponseInfo getLoginResponseInfo() {
        return loginResponseInfo;
    }

    public void setLoginResponseInfo(LoginResponseInfo loginResponseInfo) {
        this.loginResponseInfo = loginResponseInfo;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "responseCode='" + responseCode + '\'' +
                ", loginResponseStatus='" + loginResponseStatus.toString() + '\'' +
                ", userModel=" + userModel +
                ", loginResponseInfo=" + loginResponseInfo.toString() +
                '}';
    }
}
