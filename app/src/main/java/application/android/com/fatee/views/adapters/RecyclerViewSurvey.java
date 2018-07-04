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
import android.widget.Toast;


import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.Question;

public class RecyclerViewSurvey extends RecyclerView.Adapter<RecyclerViewSurvey.SurveyViewHolder> implements View.OnClickListener {
    ArrayList<Question> listQuestion;
    Context context;


    public RecyclerViewSurvey(ArrayList<Question> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_survey, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        holder.tvQuestion.setText(listQuestion.get(position).getQuestionContent());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 7, 0, 0);

        switch (listQuestion.get(position).getQuestionType()) {
            case "M":
                for (int i = 0; i < listQuestion.get(position).getArrayListAnswer().size(); i++) {
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setTextSize(17);
                    checkBox.setText(listQuestion.get(position).getArrayListAnswer().get(i).getAnswerString());
                    checkBox.setTag(listQuestion.get(position).getArrayListAnswer().get(i).getAnswerId());
                    holder.linearLayout_survey.addView(checkBox);
                    checkBox.setOnClickListener(this);
                    checkBox.setLayoutParams(params);
                    checkBox.setPadding(15, 0, 0, 0);

                }
                break;
            case "S":
                RadioGroup radioGroup = new RadioGroup(context);
                for (int i = 0; i < listQuestion.get(position).getArrayListAnswer().size(); i++) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setTextSize(17);
                    radioButton.setTag(listQuestion.get(position).getArrayListAnswer().get(i).getAnswerId());
                    radioButton.setText(listQuestion.get(position).getArrayListAnswer().get(i).getAnswerString());
                    radioGroup.addView(radioButton);
                    radioButton.setOnClickListener(this);
                    radioButton.setLayoutParams(params);
                    radioButton.setPadding(15, 0, 0, 0);

                }
                holder.linearLayout_survey.addView(radioGroup);
                break;
            case "T":
                LinearLayout.LayoutParams param_textbox = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param_textbox.setMargins(5,10,5,0);
                EditText editText = new EditText(context);
                editText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                editText.setLayoutParams(param_textbox);
                holder.linearLayout_survey.addView(editText);


                break;

            case "N":
                LinearLayout.LayoutParams param_this = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param_this.setMargins(200,0,200,0);
                NumberPicker numberPicker = new NumberPicker(context);
                numberPicker.setMaxValue(9999);
                numberPicker.setMinValue(0);
                numberPicker.setValue(10);
                numberPicker.setLayoutParams(param_this);
                holder.linearLayout_survey.addView(numberPicker);

                break;


        }
    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, String.valueOf(view.getTag()), Toast.LENGTH_SHORT).show();
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
}
