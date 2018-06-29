package application.android.com.fatee.models.entities;

import java.util.ArrayList;

public class Question {
    String questionId;
    String questionType;
    String questionContent;
    ArrayList<AnswerResponse> arrayListAnswerResponse;

    public Question(String questionId, String questionType, String questionContent, ArrayList<AnswerResponse> arrayListAnswerResponse) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.arrayListAnswerResponse = arrayListAnswerResponse;
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
    public ArrayList<AnswerResponse> getArrayListAnswerResponse() {
        return arrayListAnswerResponse;
    }

    public void setArrayListAnswerResponse(ArrayList<AnswerResponse> arrayListAnswerResponse) {
        this.arrayListAnswerResponse = arrayListAnswerResponse;
    }

}
