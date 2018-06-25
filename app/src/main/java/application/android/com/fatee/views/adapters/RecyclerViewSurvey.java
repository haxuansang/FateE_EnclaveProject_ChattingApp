package application.android.com.fatee.views.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.Question;

public class RecyclerViewSurvey extends RecyclerView.Adapter<RecyclerViewSurvey.SurveyViewHolder> {
    ArrayList<Question> listQuestion;
    Context context;


    public RecyclerViewSurvey(ArrayList<Question> listQuestion, Context context) {
        this.listQuestion = listQuestion;
        this.context = context;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.cardview_survey,parent,false);

        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        holder.tvQuestion.setText(listQuestion.get(position).getQuestionContent());

        holder.cb1.setText(listQuestion.get(position).getArrayListAnswer().get(0).getAnswerString());
        holder.cb2.setText(listQuestion.get(position).getArrayListAnswer().get(1).getAnswerString());
        holder.cb3.setText(listQuestion.get(position).getArrayListAnswer().get(2).getAnswerString());
        holder.cb4.setText(listQuestion.get(position).getArrayListAnswer().get(3).getAnswerString());

    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }

    public class SurveyViewHolder extends RecyclerView.ViewHolder{
        TextView tvQuestion;
        CheckBox cb1,cb2,cb3,cb4;


        public SurveyViewHolder(View itemView) {
            super(itemView);
            tvQuestion=(TextView)itemView.findViewById(R.id.tvQuestion);
            cb1=(CheckBox) itemView.findViewById(R.id.cbAnswer1);
            cb2=(CheckBox) itemView.findViewById(R.id.cbAnswer2);
            cb3=(CheckBox) itemView.findViewById(R.id.cbAnswer3);
            cb4=(CheckBox) itemView.findViewById(R.id.cbAnswer4);

        }
    }
}
