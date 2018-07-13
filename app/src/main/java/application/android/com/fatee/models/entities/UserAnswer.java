package application.android.com.fatee.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAnswer {
    @SerializedName("questionId")
    @Expose
    private String questionId;
    @SerializedName("answerId")
    @Expose
    private String answerId;
    @SerializedName("answerContent")
    @Expose
    private String answerContent;

    public UserAnswer(String questionId, String answerId, String answerContent) {
        this.questionId = questionId;
        this.answerId = answerId;
        this.answerContent = answerContent;
    }

    public UserAnswer() {
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
