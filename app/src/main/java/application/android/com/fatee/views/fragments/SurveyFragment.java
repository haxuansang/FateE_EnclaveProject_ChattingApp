package application.android.com.fatee.views.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quickblox.users.model.QBUser;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.QuickBloxResponse;
import application.android.com.fatee.presenters.ChattingGroupPresenter;
import application.android.com.fatee.presenters.ChattingPresenterImpl;
import application.android.com.fatee.presenters.interfaces.ChattingPresenter;
import application.android.com.fatee.utils.DiaglogConstant;
import application.android.com.fatee.utils.LoginConstant;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.models.entities.SurveyResponse;
import application.android.com.fatee.models.entities.SurveyResponseMessage;
import application.android.com.fatee.models.entities.SurveyResultResponse;
import application.android.com.fatee.presenters.SurveyPresenterImpl;
import application.android.com.fatee.presenters.interfaces.SurveyPresenter;
import application.android.com.fatee.views.MainActivity;
import application.android.com.fatee.views.adapters.RecyclerViewSurvey;
import application.android.com.fatee.views.interfaces.ChattingView;
import application.android.com.fatee.views.interfaces.OnChangeStateAddOrUpdateSurveyButtonListenter;
import application.android.com.fatee.views.interfaces.OnReceiveSurveyListener;
import application.android.com.fatee.views.interfaces.SurveyView;


public class SurveyFragment extends Fragment implements SurveyView, OnReceiveSurveyListener, OnChangeStateAddOrUpdateSurveyButtonListenter, ChattingView {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerViewSurvey recyclerViewSurvey;
    private SurveyPresenter surveyPresenter;
    private static SurveyFragment instance;
    private Button btnSumbitOrUpdate;
    private FloatingActionButton fab;
    private boolean isEdit;
    private ChattingPresenter chattingPresenter;
    private ChattingGroupPresenter chattingGroupPresenter;
    private String username;
    private String password;

    public static SurveyFragment getInstance() {
        if (instance == null)
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
        chattingGroupPresenter = new ChattingGroupPresenter();
        surveyPresenter = new SurveyPresenterImpl(this);
        chattingPresenter = new ChattingPresenterImpl(this);
        surveyPresenter.setListener(this);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        surveyPresenter.getSurvey();
        btnSumbitOrUpdate = view.findViewById(R.id.btn_submit_survey);
        btnSumbitOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = new ProgressDialog(getActivity());
                if (isEdit) {
                    pd.setMessage(DiaglogConstant.ADD_DIAGLOG_NAME);
                    pd.show();
                    getAllInfoSurvey();
                    surveyPresenter.addAnswers(recyclerViewSurvey.getAnswerRequest());
                    chattingPresenter.getQuickBloxIdFromServer();
                    pd.dismiss();
                    fab.setVisibility(View.GONE);
                    UserUtil.setSurveyStatus(SurveyConstant.USER_FINISHED_SURVEY_STATUS);
                } else {
                    pd.setMessage(DiaglogConstant.UPDATE_DIAGLOG_NAME);
                    pd.show();
                    getAllInfoSurvey();
                    surveyPresenter.editAnswers(recyclerViewSurvey.getAnswerRequest());
                    showNoticeDiaglogMessage(DiaglogConstant.UPDATE_DIAGLOG_MESSAGE);
                    pd.dismiss();
                }
            }
        });
        username = getActivity().getIntent().getExtras().getString(LoginConstant.USERNAME);
        password = getActivity().getIntent().getExtras().getString(LoginConstant.PASSWORD);
        fab = view.findViewById(R.id.edit_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_eye_black_24dp));
                    isEdit = false;
                    btnSumbitOrUpdate.setVisibility(View.VISIBLE);
                } else if (!isEdit) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mode_edit_24dp));
                    isEdit = true;
                    btnSumbitOrUpdate.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void getAllInfoSurvey() {
        recyclerViewSurvey.getAnswerRequest().getUserAnswers().clear();
        recyclerViewSurvey.addAnswerTypeText();
        recyclerViewSurvey.addAnswerTypeCheckbox();
        recyclerViewSurvey.addAnswerTypeRadioButton();
        recyclerViewSurvey.addAnswerTypeNumberPicker();
    }

    @Override
    public void viewSurvey(SurveyResponse surveyResponse) {
        String surveyStatus = UserUtil.getSurveyStatus();
        if (surveyStatus.equals(SurveyConstant.USER_NO_FINISH_SURVEY_STATUS)) {
            String surveyResponseCode = surveyResponse.getResponseCode();
            if (surveyResponseCode.equals(LoginConstant.USER_LOGIN_SUCCESS_RESPONSE_CODE)) {
                recyclerViewSurvey = new RecyclerViewSurvey(surveyResponse, view.getContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                isEdit = true;
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(recyclerViewSurvey);
                fab.setVisibility(View.GONE);
                recyclerViewSurvey.setOnChangeStateAddOrUpdateSurveyButtonListenter(this);
                btnSumbitOrUpdate.setText(R.string.submit_button_name);
                btnSumbitOrUpdate.setEnabled(false);
            }
        }

        if (surveyStatus.equals(SurveyConstant.USER_FINISHED_SURVEY_STATUS)) {
            surveyPresenter.getListener().onReceiveSurveyResultResponse(surveyResponse);
        }
    }

    @Override
    public void viewSurveyResult(SurveyResponse surveyResponse, SurveyResultResponse surveyResultResponse) {
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return isEdit;
            }
        });
        fab.setImageResource(R.drawable.ic_mode_edit_24dp);
        isEdit = true;
        recyclerViewSurvey = new RecyclerViewSurvey(surveyResponse, surveyResultResponse, view.getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewSurvey);
        btnSumbitOrUpdate.setText(R.string.update_button_name);
        btnSumbitOrUpdate.setVisibility(View.GONE);
        recyclerViewSurvey.setOnChangeStateAddOrUpdateSurveyButtonListenter(this);
    }

    private void showNoticeDiaglogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(DiaglogConstant.OK_ACTION, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void viewNotificationAfterAddSurvey(SurveyResponseMessage surveyResponseMessage) {
        fab.setVisibility(View.GONE);
        btnSumbitOrUpdate.setVisibility(View.GONE);
    }

    @Override
    public void viewNotificationAfterEditSurvey(SurveyResponseMessage surveyResponseMessage) {
    }

    @Override
    public void onReceiveSurveyResultResponse(SurveyResponse surveyResponse) {
        surveyPresenter.getAnswers(surveyResponse, UserUtil.getUserModel());
    }

    @Override
    public void onEnableAddOrUpdateButton() {
        btnSumbitOrUpdate.setEnabled(true);
    }

    @Override
    public void onDisableAddOrUpdateButton() {
        btnSumbitOrUpdate.setEnabled(false);
    }

    @Override
    public void joinUserIntoRoom(QuickBloxResponse quickBloxResponse) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        QBUser qbUser = new QBUser(username, password);
        chattingGroupPresenter.loadBitmapUsers();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        chattingGroupPresenter.createSessionForChat(quickBloxResponse.getQuickBloxId(), qbUser, progressDialog, fragmentTransaction);
    }
}