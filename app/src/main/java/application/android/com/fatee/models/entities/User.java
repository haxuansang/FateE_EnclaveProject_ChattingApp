package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("confirmPassword")
    @Expose
    private String confirmPassword;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("gender")
    @Expose
    private Boolean gender;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("quickBloxChatId")
    @Expose
    private String quickBloxChatId;

    public User(String username, String password, String confirmPassword, String mail, String nickname, Boolean gender, String description, String avatar, String quickBloxChatId) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.mail = mail;
        this.nickname = nickname;
        this.gender = gender;
        this.description = description;
        this.avatar = avatar;
        this.quickBloxChatId = quickBloxChatId;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String mail, String nickname, Boolean gender) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.mail = mail;
        this.nickname = nickname;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQuickBloxChatId() {
        return quickBloxChatId;
    }

    public void setQuickBloxChatId(String quickBloxChatId) {
        this.quickBloxChatId = quickBloxChatId;
    }
}