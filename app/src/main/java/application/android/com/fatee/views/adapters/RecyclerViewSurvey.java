package application.android.com.fatee.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.android.com.fatee.R;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.models.entities.AnswerRequest;
import application.android.com.fatee.models.entities.QuestionResponse;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.models.entities.UserAnswer;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.views.interfaces.OnChangeStateAddOrUpdateSurveyButtonListenter;

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
    private ArrayList<NumberPicker> numberPickers;
    private int questionCount = 1;
    private OnChangeStateAddOrUpdateSurveyButtonListenter onChangeStateAddOrUpdateSurveyButtonListenter;

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
        numberPickers = new ArrayList<>();
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
        if (surveyResponse.getQuestionResponses().size() >= questionCount) {
            switch (surveyResponse.getQuestionResponses().get(position).getType()) {
                case SurveyConstant.QUESTION_CHECK_BOX_TYPE:
                    showCheckBoxOfAnswers(position, answerSize, questionResponse, params, holder);
                    break;
                case SurveyConstant.QUESTION_RADIO_BUTTON_TYPE:
                    showRadioButton(position, answerSize, questionResponse, params, holder);
                    break;
                case SurveyConstant.QUESTION_EDIT_TEXT_TYPE:
                    showEditText(position, questionResponse, holder);
                    break;
                case SurveyConstant.QUESTION_NUMBER_PICKER_TYPE:
                    showNumberPicker(holder, questionResponse, position);
                    break;
            }
            questionCount++;
        }
    }

    private void showCheckBoxOfAnswers(final int position, final int answerSize, final QuestionResponse questionResponse, LinearLayout.LayoutParams params, SurveyViewHolder holder) {
        for (int indexOfAnswer = 0; indexOfAnswer < answerSize; indexOfAnswer++) {
            final CheckBox checkBoxOfAnswer = new CheckBox(context);
            checkBoxOfAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        surveyResponse.getQuestionResponses().get(position).setHasAnswered(true);
                        checkSurveyValid();
                    } else {
                        int count = 0;
                        for (int i = 0; i < checkboxs.size(); i++) {
                            if (checkboxs.get(i).isChecked()) {
                                count++;
                            }
                        }
                        if (count == checkboxs.size()) {
                            surveyResponse.getQuestionResponses().get(position).setHasAnswered(false);
                        }
                    }
                }
            });
            checkBoxInitialize(checkBoxOfAnswer, questionResponse, indexOfAnswer, holder, params);
            showAnswersOfCheckBoxTypeAfterGetAsnwersFromServer(checkBoxOfAnswer, questionResponse, indexOfAnswer);
        }
    }

    private void checkSurveyValid() {
        boolean hasAnswered = true;
        for (QuestionResponse q : surveyResponse.getQuestionResponses()) {
            if (!q.isHasAnswered()) {
                hasAnswered = false;
                break;
            }
        }
        if (hasAnswered) {
            onChangeStateAddOrUpdateSurveyButtonListenter.onEnableAddOrUpdateButton();
        } else {
            onChangeStateAddOrUpdateSurveyButtonListenter.onDisableAddOrUpdateButton();
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
            String questionType = questionResponse.getType();
            for (int i = 0; i < surveyResultSize && questionType.equals(SurveyConstant.QUESTION_CHECK_BOX_TYPE); i++) {
                String answeredId = surveyResultResponse.getUserAnswers().get(i).getAnswerId();
                if (answeredId != null) {
                    String answerId = questionResponse.getAnswerResponses().get(indexOfAnswer).getId();
                    if (answeredId.equals(answerId)) {
                        checkBox.setChecked(true);
                    }
                }
            }
        }
    }

    private void showRadioButton(final int position, int answerSize, final QuestionResponse questionResponse, LinearLayout.LayoutParams params, SurveyViewHolder holder) {
        RadioGroup radioGroup = new RadioGroup(context);
        for (int indexOfAnswer = 0; indexOfAnswer < answerSize; indexOfAnswer++) {
            RadioButton radioButton = new RadioButton(context);
            radioButtonInitialize(radioButton, radioGroup, questionResponse, indexOfAnswer, holder, params);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        surveyResponse.getQuestionResponses().get(position).setHasAnswered(true);
                        checkSurveyValid();
                    }
                }
            });
            showAnswersOfRadioButtonTypeAfterGetAsnwersFromServer(radioButton, questionResponse, indexOfAnswer);
        }
        holder.linearLayout_survey.addView(radioGroup);
    }

    private void showAnswersOfRadioButtonTypeAfterGetAsnwersFromServer(RadioButton radioButtonOfAnswer, QuestionResponse questionResponse, int indexOfAnswer) {
        if (surveyResultResponse != null) {
            int surveyResultSize = surveyResultResponse.getUserAnswers().size();
            String questionType = questionResponse.getType();
            for (int i = 0; i < surveyResultSize && questionType.equals(SurveyConstant.QUESTION_RADIO_BUTTON_TYPE); i++) {
                String userAnsweredId = surveyResultResponse.getUserAnswers().get(i).getAnswerId();
                if (userAnsweredId != null) {
                    String userAnswerId = questionResponse.getAnswerResponses().get(indexOfAnswer).getId();
                    if (userAnsweredId.equals(userAnswerId)) {
                        radioButtonOfAnswer.setChecked(true);
                    }
                }
            }
        }
    }

    private void radioButtonInitialize(RadioButton radioButtonOfAnswer, RadioGroup radioGroup, QuestionResponse questionResponse, int indexOfAnswer, SurveyViewHolder holder, LinearLayout.LayoutParams params) {
        radioButtonOfAnswer.setTextSize(17);
        radioButtonOfAnswer.setTag(questionResponse.getAnswerResponses().get(indexOfAnswer).getId() + "%" + questionResponse.getId());
        radioButtonOfAnswer.setText(questionResponse.getAnswerResponses().get(indexOfAnswer).getAnswer());
        radiobuttons.add(radioButtonOfAnswer);
        radioGroup.addView(radioButtonOfAnswer);
        radioButtonOfAnswer.setOnClickListener(this);
        radioButtonOfAnswer.setLayoutParams(params);
        radioButtonOfAnswer.setPadding(15, 0, 0, 0);
    }


    private void showEditText(final int position, final QuestionResponse questionResponse, SurveyViewHolder holder) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final EditText editText = new EditText(context);
        editTextInitialize(editText, questionResponse, holder, params);
        editTextChangeValueEvent(editText, position);
        showAnswersOfEditTextTypeAfterGetAsnwersFromServer(editText, questionResponse);
    }

    private void showAnswersOfEditTextTypeAfterGetAsnwersFromServer(EditText editTextOfAnswer, QuestionResponse questionResponse) {
        if (surveyResultResponse != null) {
            if (surveyResultResponse != null) {
                int surveyResultSize = surveyResultResponse.getUserAnswers().size();
                String questionType = questionResponse.getType();
                for (int i = 0; i < surveyResultSize && questionType.equals(SurveyConstant.QUESTION_EDIT_TEXT_TYPE); i++) {
                    String questionIdOfAnswered = surveyResultResponse.getUserAnswers().get(i).getQuestionId();
                    String questionId = questionResponse.getId();
                    if (questionId.equals(questionIdOfAnswered)) {
                        String answerContent = surveyResultResponse.getUserAnswers().get(i).getAnswerContent();
                        editTextOfAnswer.setText(answerContent);
                    }
                }
            }
        }
    }

    private void editTextChangeValueEvent(final EditText editText, final int position) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!"".equals(editable.toString())) {
                    surveyResponse.getQuestionResponses().get(position).setHasAnswered(true);
                    checkSurveyValid();
                } else {
                    surveyResponse.getQuestionResponses().get(position).setHasAnswered(false);
                    checkSurveyValid();
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String valueOfEditText = editText.getText().toString();
                    userAnswer.setAnswerContent(valueOfEditText);
                    answerRequest.getUserAnswers().add(userAnswer);
                } else {
                    List<UserAnswer> userAnswers = answerRequest.getUserAnswers();
                    String answerContent = userAnswer.getAnswerContent();
                    if (userAnswers.contains(userAnswer) && answerContent != null) {
                        userAnswers.remove(userAnswer);
                    }
                }
            }
        });
    }

    private void editTextInitialize(EditText editTextOfAnswer, QuestionResponse questionResponse, SurveyViewHolder holder, LinearLayout.LayoutParams params) {
        params.setMargins(5, 10, 5, 0);
        editTextOfAnswer.setGravity(View.TEXT_ALIGNMENT_CENTER);
        editTextOfAnswer.setLayoutParams(params);
        holder.linearLayout_survey.addView(editTextOfAnswer);
        userAnswer = new UserAnswer();
        userAnswer.setQuestionId(questionResponse.getId());
        answerOfTextTypes.add(userAnswer);
        editTexts.add(editTextOfAnswer);
    }

    private void showNumberPicker(SurveyViewHolder holder, QuestionResponse questionResponse, final int position) {
        LinearLayout.LayoutParams param_this = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        NumberPicker numberPicker = new NumberPicker(context);
        numberPickerInitialize(param_this, numberPicker);
        userAnswer = new UserAnswer();
        userAnswer.setQuestionId(questionResponse.getId());
        questionOfNumberTypes.add(questionResponse.getId());
        numberPickers.add(numberPicker);
        answerRequest.getUserAnswers().add(userAnswer);
        surveyResponse.getQuestionResponses().get(position).setHasAnswered(true);
        showAnswersOfNumberPickerTypeAfterGetAsnwersFromServer(numberPicker, questionResponse);
        numberPicker.setLayoutParams(param_this);
        holder.linearLayout_survey.addView(numberPicker);
    }

    private void numberPickerInitialize(LinearLayout.LayoutParams param_this, NumberPicker numberPicker) {
        param_this.setMargins(200, 0, 200, 0);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
    }

    private void showAnswersOfNumberPickerTypeAfterGetAsnwersFromServer(NumberPicker numberPicker, QuestionResponse questionResponse) {
        if (surveyResultResponse != null) {
            if (surveyResultResponse != null) {
                int surveyResultSize = surveyResultResponse.getUserAnswers().size();
                String questionType = questionResponse.getType();
                for (int i = 0; i < surveyResultSize && questionType.equals(SurveyConstant.QUESTION_NUMBER_PICKER_TYPE); i++) {
                    String questionOfAnsweredId = surveyResultResponse.getUserAnswers().get(i).getQuestionId();
                    String questionId = questionResponse.getId();
                    if (questionOfAnsweredId.equals(questionId)) {
                        String answerContent = surveyResultResponse.getUserAnswers().get(i).getAnswerContent().trim();
                        numberPicker.setValue(Integer.parseInt(answerContent));
                    }
                }
            }
        }
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
                String question = tag.split(SurveyConstant.TAG_DELIM)[1];
                String answer = tag.split(SurveyConstant.TAG_DELIM)[0];
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
                String question = tag.split(SurveyConstant.TAG_DELIM)[1];
                String answer = tag.split(SurveyConstant.TAG_DELIM)[0];
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
            int answer = numberPickers.get(i).getValue();
            userAnswer.setQuestionId(question);
            userAnswer.setAnswerContent("" + answer);
            answerRequest.getUserAnswers().add(userAnswer);
        }
    }

    public void setOnChangeStateAddOrUpdateSurveyButtonListenter(OnChangeStateAddOrUpdateSurveyButtonListenter onChangeStateAddOrUpdateSurveyButtonListenter) {
        this.onChangeStateAddOrUpdateSurveyButtonListenter = onChangeStateAddOrUpdateSurveyButtonListenter;
    }
}
