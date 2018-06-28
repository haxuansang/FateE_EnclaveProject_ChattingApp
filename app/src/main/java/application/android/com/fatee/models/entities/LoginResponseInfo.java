package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseInfo{
    @SerializedName("resetPassLink")
    @Expose
    private String resetPassLink;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("mail")
    @Expose
    private String mail;

    public LoginResponseInfo() {
    }

    public String getResetPassLink() {
        return resetPassLink;
    }

    public void setResetPassLink(String resetPassLink) {
        this.resetPassLink = resetPassLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
