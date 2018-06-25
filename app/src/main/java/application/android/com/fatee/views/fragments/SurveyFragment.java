package application.android.com.fatee.views.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.Answer;
import application.android.com.fatee.models.entities.Question;
import application.android.com.fatee.views.adapters.RecyclerViewSurvey;


public class SurveyFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RecyclerViewSurvey recyclerViewSurvey;
    public SurveyFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_survey, container, false);
        InitFragment();
        return view;
    }

    public void InitFragment()
    {
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        ArrayList<Answer> listAnswer  = new ArrayList<>();
        listAnswer.add(new Answer(1,"Football"));
        listAnswer.add(new Answer(2,"Basketbal"));
        listAnswer.add(new Answer(3,"Tennis"));
        listAnswer.add(new Answer(4,"Swimming"));
        ArrayList<Question> listQuestions= new ArrayList<>();
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question(1,"Checkbox","What type of sports do you like?",listAnswer));

        recyclerViewSurvey = new RecyclerViewSurvey(listQuestions,view.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewSurvey);







    }





}
