package application.android.com.fatee.views.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.AnswerResponse;
import application.android.com.fatee.models.entities.Question;
import application.android.com.fatee.models.entities.QuestionResponse;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.presenters.SurveyPresenterImpl;
import application.android.com.fatee.presenters.interfaces.SurveyPresenter;
import application.android.com.fatee.views.adapters.RecyclerViewSurvey;
import application.android.com.fatee.views.interfaces.SurveyView;


public class SurveyFragment extends Fragment implements SurveyView {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewSurvey recyclerViewSurvey;
    private SurveyPresenter surveyPresenter;

    public SurveyFragment() {
        super();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_survey, container, false);
        surveyPresenter = new SurveyPresenterImpl(this);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        surveyPresenter.getSurvey();
        return view;
    }


    @Override
    public void viewSurvey(SurveyResponse surveyResponse) {
        if("success".equals(surveyResponse.getResponseCode())) {
            recyclerViewSurvey = new RecyclerViewSurvey(surveyResponse, view.getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerViewSurvey);
        }
    }
}
