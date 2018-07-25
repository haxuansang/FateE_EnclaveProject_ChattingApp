package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("quickBloxChatId")
    @Expose
    private String quickBloxChatId;
    @SerializedName("gender")
    @Expose
    private Boolean gender;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("userStatus")
    @Expose
    private String userStatus;
    @SerializedName("banStatus")
    @Expose
    private String banStatus;
    @SerializedName("reportNumbers")
    @Expose
    private Integer reportNumbers;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("surveyStatus")
    @Expose
    private String surveyStatus;

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirebaseChatId() {
        return quickBloxChatId;
    }

    public void setFirebaseChatId(String firebaseChatId) {
        this.quickBloxChatId = firebaseChatId;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public Integer getReportNumbers() {
        return reportNumbers;
    }

    public void setReportNumbers(Integer reportNumbers) {
        this.reportNumbers = reportNumbers;
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

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public UserModel(String id, String nickname, String description, String avatar, Boolean gender) {
        this.id = id;
        this.nickname = nickname;
        this.description = description;
        this.avatar = avatar;
        this.gender = gender;
    }
}
