package application.android.com.fatee.models.entities;

import java.util.ArrayList;

public class Question {
    String questionId;
    String questionType;
    String questionContent;
    ArrayList<Answer> arrayListAnswer;

    public Question(String questionId, String questionType, String questionContent, ArrayList<Answer> arrayListAnswer) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.arrayListAnswer = arrayListAnswer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
    public ArrayList<Answer> getArrayListAnswer() {
        return arrayListAnswer;
    }

    public void setArrayListAnswer(ArrayList<Answer> arrayListAnswer) {
        this.arrayListAnswer = arrayListAnswer;
    }

}
