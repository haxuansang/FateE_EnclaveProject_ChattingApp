package application.android.com.fatee.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.helpers.UserUtil;
import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.QuestionResponse;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.UserAnswer;
import application.android.com.fatee.utils.SurveyConstant;

public class RecyclerViewSurvey extends RecyclerView.Adapter<RecyclerViewSurvey.SurveyViewHolder> implements View.OnClickListener {
    private SurveyResponse surveyResponse;
    private SurveyResultResponse surveyResultResponse;
    private Context context;
    private AnswerRequest answerRequest;
    private UserAnswer userAnswer;
    private ArrayList<EditText> editTexts;
    private ArrayList<CheckBox> checkboxs;
    private ArrayList<RadioButton> radiobuttons;
    private ArrayList<UserAnswer> answerOfTextTypes;
    private ArrayList<String> questionOfNumberTypes;
    private ArrayList<NumberPicker> answerOfNumberTypes;

    public RecyclerViewSurvey(SurveyResponse surveyResponse, Context context) {
        this.surveyResponse = surveyResponse;
        this.context = context;
        createListToContainAnswers();
    }

    public RecyclerViewSurvey(SurveyResponse surveyResponse, SurveyResultResponse surveyResultResponse, Context context) {
        this.surveyResultResponse = surveyResultResponse;
        this.context = context;
        this.surveyResponse = surveyResponse;
        createListToContainAnswers();
    }

    private void createListToContainAnswers() {
        answerRequest = new AnswerRequest();
        editTexts = new ArrayList<>();
        answerOfTextTypes = new ArrayList<>();
        checkboxs = new ArrayList<>();
        radiobuttons = new ArrayList<>();
        questionOfNumberTypes = new ArrayList<>();
        answerOfNumberTypes = new ArrayList<>();
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_survey, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        holder.tvQuestion.setText(surveyResponse.getQuestionResponses().get(position).getQuestion());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 7, 0, 0);
        QuestionResponse questionResponse = surveyResponse.getQuestionResponses().get(position);
        answerRequest.setUserId(UserUtil.getUser().getId());
        showFormAfterGetQuestionsAndAnswers(position, questionResponse, layoutParams, holder);
    }

    private void showFormAfterGetQuestionsAndAnswers(int position, QuestionResponse questionResponse, LinearLayout.LayoutParams params, SurveyViewHolder holder) {
        int answerSize = surveyResponse.getQuestionResponses().get(position).getAnswerResponses().size();
        switch (surveyResponse.getQuestionResponses().get(position).getType()) {
            case "M":
                showCheckBoxOfAnswers(answerSize, questionResponse, params, holder);
                break;
            case "S":
                showRadioButton(answerSize, questionResponse, params, holder);
                break;
            case "T":
                showEditText(questionResponse, holder);
                break;
            case "N":
                showNumberPicker(holder, questionResponse, position);
                break;
        }
    }

    private void showCheckBoxOfAnswers(final int answerSize, final QuestionResponse questionResponse, LinearLayout.LayoutParams params, SurveyViewHolder holder) {
        for (int indexOfAnswer = 0; indexOfAnswer < answerSize; indexOfAnswer++) {
            final CheckBox checkBoxOfAnswer = new CheckBox(context);
            checkBoxInitialize(checkBoxOfAnswer, questionResponse, indexOfAnswer, holder, params);
            showAnswersOfCheckBoxTypeAfterGetAsnwersFromServer(checkBoxOfAnswer, questionResponse, indexOfAnswer);
        }
    }

    private void checkBoxInitialize(CheckBox checkBoxOfAnswer, QuestionResponse questionResponse, int indexOfAnswer, SurveyViewHolder holder, LinearLayout.LayoutParams params) {
        checkBoxOfAnswer.setTextSize(17);
        String answerContent = questionResponse.getAnswerResponses().get(indexOfAnswer).getAnswer();
        checkBoxOfAnswer.setText(answerContent);
        String answerId = questionResponse.getAnswerResponses().get(indexOfAnswer).getId();
        String questionId = questionResponse.getId();
        String answerCheckBoxTag = answerId + SurveyConstant.TAG_DELIM + questionId;
        checkBoxOfAnswer.setTag(answerCheckBoxTag);
        checkboxs.add(checkBoxOfAnswer);
        holder.linearLayout_survey.addView(checkBoxOfAnswer);
        checkBoxOfAnswer.setOnClickListener(this);
        checkBoxOfAnswer.setLayoutParams(params);
        checkBoxOfAnswer.setPadding(15, 0, 0, 0);
    }

    private void showAnswersOfCheckBoxTypeAfterGetAsnwersFromServer(CheckBox checkBox, QuestionResponse questionResponse, int indexOfAnswer) {
        if (surveyResultResponse != null) {
            int surveyResultSize = surveyResultResponse.getUserAnswers().size();
            for (int j = 0; j < surveyResultSize && "M".equals(questionResponse.getType()); j++) {
                if (surveyResultResponse.getUserAnswers().get(j).getAnswerId() != null) {
                    if (surveyResultResponse.getUserAnswers().get(j).getAnswerId().equals(questionResponse.getAnswerResponses().get(indexOfAnswer).getId())) {
                        checkBox.setChecked(true);
                    }
                }
            }
        }
    }

    private void showRadioButton(int answerSize, final QuestionResponse questionResponse, LinearLayout.LayoutParams params, SurveyViewHolder holder) {
        RadioGroup radioGroup = new RadioGroup(context);
        for (int i = 0; i < answerSize; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setTextSize(17);
            radioButton.setTag(questionResponse.getAnswerResponses().get(i).getId() + "%" + questionResponse.getId());
            radioButton.setText(questionResponse.getAnswerResponses().get(i).getAnswer());
            radiobuttons.add(radioButton);
            radioGroup.addView(radioButton);
            radioButton.setOnClickListener(this);
            radioButton.setLayoutParams(params);
            radioButton.setPadding(15, 0, 0, 0);
            if (surveyResultResponse != null) {
                int surveyResultSize = surveyResultResponse.getUserAnswers().size();
                for (int j = 0; j < surveyResultSize && "S".equals(questionResponse.getType()); j++) {
                    if (surveyResultResponse.getUserAnswers().get(j).getAnswerId() != null) {
                        if (surveyResultResponse.getUserAnswers().get(j).getAnswerId().equals(questionResponse.getAnswerResponses().get(i).getId())) {
                            radioButton.setChecked(true);
                        }
                    }
                }
            }
        }
        holder.linearLayout_survey.addView(radioGroup);
    }


    private void showEditText(final QuestionResponse questionResponse, SurveyViewHolder holder) {
        LinearLayout.LayoutParams param_textbox = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        param_textbox.setMargins(5, 10, 5, 0);
        final EditText editText = new EditText(context);
        editText.setGravity(View.TEXT_ALIGNMENT_CENTER);
        editText.setLayoutParams(param_textbox);
        holder.linearLayout_survey.addView(editText);

        userAnswer = new UserAnswer();
        userAnswer.setQuestionId(questionResponse.getId());
        answerOfTextTypes.add(userAnswer);
        editTexts.add(editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    userAnswer.setAnswerContent(editText.getText().toString());
                    answerRequest.getUserAnswers().add(userAnswer);
                } else {
                    if (answerRequest.getUserAnswers().contains(userAnswer) && userAnswer.getAnswerContent() != null) {
                        answerRequest.getUserAnswers().remove(userAnswer);
                    }
                }
            }
        });
        if (surveyResultResponse != null) {
            if (surveyResultResponse != null) {
                int surveyResultSize = surveyResultResponse.getUserAnswers().size();
                for (int i = 0; i < surveyResultSize && "T".equals(questionResponse.getType()); i++) {
                    if (surveyResultResponse.getUserAnswers().get(i).getQuestionId().equals(questionResponse.getId())) {
                        editText.setText(surveyResultResponse.getUserAnswers().get(i).getAnswerContent());
                    }
                }
            }
        }
    }

    private void showNumberPicker(SurveyViewHolder holder, QuestionResponse questionResponse, int position) {
        LinearLayout.LayoutParams param_this = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        param_this.setMargins(200, 0, 200, 0);
        NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMaxValue(9999);
        numberPicker.setMinValue(0);
        userAnswer = new UserAnswer();
        userAnswer.setQuestionId(questionResponse.getId());
        questionOfNumberTypes.add(questionResponse.getId());
        answerOfNumberTypes.add(numberPicker);
        answerRequest.getUserAnswers().add(userAnswer);
        if (surveyResultResponse != null) {
            if (surveyResultResponse != null) {
                int surveyResultSize = surveyResultResponse.getUserAnswers().size();
                for (int i = 0; i < surveyResultSize && "N".equals(questionResponse.getType()); i++) {
                    if (surveyResultResponse.getUserAnswers().get(i).getQuestionId().equals(questionResponse.getId())) {
                        numberPicker.setValue(Integer.parseInt(surveyResultResponse.getUserAnswers().get(i).getAnswerContent().trim()));
                    }
                }
            }
        }
        numberPicker.setLayoutParams(param_this);
        holder.linearLayout_survey.addView(numberPicker);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if (surveyResponse == null) {
            return surveyResultResponse.getUserAnswers().size();
        } else {
            return surveyResponse.getQuestionResponses().size();
        }
    }

    @Override
    public void onClick(View view) {
    }


    public class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        LinearLayout linearLayout_survey;

        public SurveyViewHolder(View itemView) {
            super(itemView);
            linearLayout_survey = (LinearLayout) itemView.findViewById(R.id.linear_survey);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
        }
    }

    public AnswerRequest getAnswerRequest() {
        return answerRequest;
    }

    public void addAnswerTypeText() {
        for (int i = 0; i < editTexts.size(); i++) {
            answerOfTextTypes.get(i).setAnswerContent(editTexts.get(i).getText().toString());
            answerRequest.getUserAnswers().add(answerOfTextTypes.get(i));
        }
    }

    public void addAnswerTypeCheckbox() {
        for (int i = 0; i < checkboxs.size(); i++) {
            if (checkboxs.get(i).isChecked()) {
                UserAnswer userAnswer = new UserAnswer();
                String tag = (String) checkboxs.get(i).getTag();
                String question = tag.split("%")[1];
                String answer = tag.split("%")[0];
                userAnswer.setQuestionId(question);
                userAnswer.setAnswerId(answer);
                answerRequest.getUserAnswers().add(userAnswer);
            }
        }
    }

    public void addAnswerTypeRadioButton() {
        for (int i = 0; i < radiobuttons.size(); i++) {
            if (radiobuttons.get(i).isChecked()) {
                UserAnswer userAnswer = new UserAnswer();
                String tag = (String) radiobuttons.get(i).getTag();
                String question = tag.split("%")[1];
                String answer = tag.split("%")[0];
                userAnswer.setQuestionId(question);
                userAnswer.setAnswerId(answer);
                answerRequest.getUserAnswers().add(userAnswer);
            }
        }
    }

    public void addAnswerTypeNumberPicker() {
        for (int i = 0; i < questionOfNumberTypes.size(); i++) {
            UserAnswer userAnswer = new UserAnswer();
            String question = questionOfNumberTypes.get(i);
            int answer = answerOfNumberTypes.get(i).getValue();
            userAnswer.setQuestionId(question);
            userAnswer.setAnswerContent("" + answer);
            answerRequest.getUserAnswers().add(userAnswer);
        }
    }


}
