package application.android.com.fatee.models.entities;

public class Answer {
    String answerId;
    String answerString;

    public Answer(String answerId, String answerString) {
        this.answerId = answerId;
        this.answerString = answerString;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
