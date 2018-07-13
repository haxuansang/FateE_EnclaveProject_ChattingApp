package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnswerRequest {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userAnswerPackage")
    @Expose
    private List<UserAnswer> userAnswers;

    public AnswerRequest() {
        userAnswers = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswer> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
