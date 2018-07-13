package application.android.com.fatee.views.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import application.android.com.fatee.R;
import application.android.com.fatee.helpers.UserUtil;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.presenters.SurveyPresenterImpl;
import application.android.com.fatee.presenters.interfaces.SurveyPresenter;
import application.android.com.fatee.views.adapters.RecyclerViewSurvey;
import application.android.com.fatee.views.interfaces.OnReceiveSurveyListener;
import application.android.com.fatee.views.interfaces.SurveyView;


public class SurveyFragment extends Fragment implements SurveyView, OnReceiveSurveyListener{
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewSurvey recyclerViewSurvey;
    private SurveyPresenter surveyPresenter;
    private static SurveyFragment instance;
    private Button btnSumbit;
    private FloatingActionButton fab;

    public static SurveyFragment getInstance() {
        if(instance == null)
            instance = new SurveyFragment();
        return instance;
    }

    public SurveyFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_survey, container, false);
        surveyPresenter = new SurveyPresenterImpl(this);
        surveyPresenter.setListener(this);
        recyclerView = view.findViewById(R.id.recyclerview);

        surveyPresenter.getSurvey();
        btnSumbit = view.findViewById(R.id.btn_submit_survey);
        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewSurvey.getAnswerRequest().getUserAnswers().clear();
                recyclerViewSurvey.addAnswerTypeText();
                recyclerViewSurvey.addAnswerTypeCheckbox();
                recyclerViewSurvey.addAnswerTypeRadioButton();
                recyclerViewSurvey.addAnswerTypeNumberPicker();
                surveyPresenter.addAnswers(recyclerViewSurvey.getAnswerRequest());
            }
        });
        fab = view.findViewById(R.id.edit_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewSurvey.getAnswerRequest().getUserAnswers().clear();
                recyclerViewSurvey.addAnswerTypeText();
                recyclerViewSurvey.addAnswerTypeCheckbox();
                recyclerViewSurvey.addAnswerTypeRadioButton();
                recyclerViewSurvey.addAnswerTypeNumberPicker();
                surveyPresenter.editAnswers(recyclerViewSurvey.getAnswerRequest());
            }
        });
        return view;
    }


    @Override
    public void viewSurvey(SurveyResponse surveyResponse) {
        if("F".equals(UserUtil.getSurveyStatus())) {
            if ("Success".equals(surveyResponse.getResponseCode())) {
                recyclerViewSurvey = new RecyclerViewSurvey(surveyResponse, view.getContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerViewSurvey);
                fab.setVisibility(View.GONE);
            }
        }
        if("T".equals(UserUtil.getSurveyStatus())) {
            surveyPresenter.getListener().onReceiveSurveyResultResponse(surveyResponse);
        }
    }

    @Override
    public void viewSurveyResult(SurveyResponse surveyResponse, SurveyResultResponse surveyResultResponse) {
        recyclerViewSurvey = new RecyclerViewSurvey(surveyResponse, surveyResultResponse, view.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewSurvey);
        btnSumbit.setVisibility(View.GONE);
    }

    @Override
    public void viewNotificationAfterAddSurvey(SurveyResponseMessage surveyResponseMessage) {
    }

    @Override
    public void viewNotificationAfterEditSurvey(SurveyResponseMessage surveyResponseMessage) {

    }

    @Override
    public void onReceiveSurveyResultResponse(SurveyResponse surveyResponse) {
        surveyPresenter.getAnswers(surveyResponse, UserUtil.getUser());
    }
}
