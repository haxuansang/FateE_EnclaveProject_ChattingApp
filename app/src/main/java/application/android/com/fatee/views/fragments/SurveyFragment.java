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
        ArrayList<Answer> listAnswer = new ArrayList<>();
        listAnswer.add(new Answer("a1","Football"));
        listAnswer.add(new Answer("a2","Basketbal"));
        listAnswer.add(new Answer("a3","Tennis"));
        listAnswer.add(new Answer("a4","Swimming"));
        ArrayList<Answer> listAnswer1= new ArrayList<>();
        listAnswer1.add(new Answer("a5","Male"));
        listAnswer1.add(new Answer("a6","Female"));
        listAnswer1.add(new Answer("a7","LGBT"));
        ArrayList<Question> listQuestions= new ArrayList<>();
        listQuestions.add(new Question("q1","M","What type of sports do you like?",listAnswer));
        listQuestions.add(new Question("q2","S","What is your gender?",listAnswer1));
        listQuestions.add(new Question ("q3","T","What your name?",null));
        listQuestions.add(new Question("q4","N","How old are you?",null));
        recyclerViewSurvey = new RecyclerViewSurvey(listQuestions,view.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewSurvey);







    }





}
