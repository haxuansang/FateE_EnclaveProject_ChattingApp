package application.android.com.fatee.models.entities;

public class Answer {
    long answerId;
    String answerString;

    public Answer(long answerId, String answerString) {
        this.answerId = answerId;
        this.answerString = answerString;
    }

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
