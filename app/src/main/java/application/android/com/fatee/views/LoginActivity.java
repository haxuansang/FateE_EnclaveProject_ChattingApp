package application.android.com.fatee.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.android.com.fatee.R;
import application.android.com.fatee.helpers.UserUtil;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.CheckTempBannedUserService;
import application.android.com.fatee.presenters.ProcessLogicPresenterImpl;
import application.android.com.fatee.utils.ConnectionBroadcastReceiver;
import application.android.com.fatee.utils.Constant;
import application.android.com.fatee.views.fragments.SurveyFragment;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class LoginActivity extends AppCompatActivity implements ViewProcessLogin, View.OnFocusChangeListener {
    private Button btnLogin, btnRegister;
    private EditText edtUsername, edtPassword;
    private RelativeLayout relativeLayoutLogin;
    private LinearLayout linearLayoutWrongLogin;
    Handler handler;
    ProcessLogicPresenterImpl presenterLogicProcessLogin;
    public static ViewProcessLogin viewProcessLogin;
    BroadcastReceiver broadcastReceiver;
    private int countdown = 5;
    private int count = 0;
    private int wrong_info_times = 0;
    SharedPreferences sharedPreferences;
    Intent service;
    private String preUsername = "";
    private ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewProcessLogin = this;
        delayLogin();
        initView();
        initFocusChangeListener();
        presenterLogicProcessLogin = new ProcessLogicPresenterImpl(viewProcessLogin);
        broadcastReceiver = new ConnectionBroadcastReceiver();
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        service = new Intent(LoginActivity.this, CheckTempBannedUserService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        startService(service);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtUsername.setOnFocusChangeListener(this);
        edtPassword.setOnFocusChangeListener(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

    }


    // Init app View
    private void initView() {
        imageViewIcon=(ImageView)findViewById(R.id.imgview_icon);
        btnLogin=(Button)findViewById(R.id.btn_Login);
        btnRegister=(Button)findViewById(R.id.btn_Register);
        edtPassword=(EditText)findViewById(R.id.edt_Password);
        edtUsername=(EditText)findViewById(R.id.edt_Username);
        relativeLayoutLogin=(RelativeLayout)findViewById(R.id.relativeLayout_Login);
        linearLayoutWrongLogin=(LinearLayout)findViewById(R.id.linear_warning);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(service);
    }


    private void delayLogin() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayoutLogin.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    @Override
    public void noticeForUserAfterLogin(LoginResponse loginResponse) {
        if (!isTempBannedUser()) {
            String status = getUserStatus(loginResponse);
            if ("banned".equals(status)) {
                showNoticeDiaglogMessage("This user was banned!");
            } else if ("success".equals(status)) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                UserUtil.getInstance(loginResponse.getUserModel(), loginResponse.getUserModel().getSurveyStatus());
                intent.putExtra("surveyStatus", loginResponse.getUserModel().getSurveyStatus());
                intent.putExtra("userId", loginResponse.getUserModel().getId());
                this.startActivity(intent);
                finish();
            } else if ("WUWPFailed".equals(status)) {
                showNoticeDiaglogMessage("Wrong username!");
            } else {
                if(count == 3) {
                    showOptionDiaglogMessage(loginResponse.getLoginResponseInfo().getMessage(), loginResponse.getLoginResponseInfo().getMail());
                } else if(count < 3){
                    showNoticeDiaglogMessage("Wrong password!");
                    limitLoginNumber();
                }
            }
        }
    }

    @Override
    public void caseLostNetworkConnection() {
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundResource(R.drawable.disableshape);
        linearLayoutWrongLogin.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), Constant.LOST_INTERNET_CONNECTION, Toast.LENGTH_SHORT);
    }

    @Override
    public void caseHaveNetworkConnection() {
        edtUsername.setEnabled(true);
        edtPassword.setEnabled(true);
        btnLogin.setEnabled(true);
        btnLogin.setBackgroundResource(R.drawable.shape);
        linearLayoutWrongLogin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus)
            hideSoftInputKeyboard(view);
    }

    private void hideSoftInputKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initFocusChangeListener() {
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtPassword.hasFocus()) {
                    if (!isValidPassword(edtPassword.getText().toString())) {
                        edtPassword.setError(Constant.PASSWORD_ERROR);
                    }
                }
            }
        });

        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtUsername.hasFocus()) {
                    if (!isValidUsername(edtUsername.getText().toString())) {
                        edtUsername.setError(Constant.USERNAME_ERROR);
                    }
                }
            }
        });
    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidUsername(String username) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(Constant.USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public void login(View v) {
        String currentUsername = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if(!preUsername.equals(currentUsername)) {
            count = 0;
            wrong_info_times = 0;
            countdown = 5;
        }
        if(!"".equals(currentUsername) && !"".equals(password)) {
            presenterLogicProcessLogin.getDataFromServer(new User(currentUsername, password));
            preUsername = currentUsername;
        } else {
            showNoticeDiaglogMessage("Please enter username and password!");
        }
    }

    private void showOptionDiaglogMessage(String message, final String mail) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showNoticeDiaglogMessage("Please check mail " + hideMail(mail) + " to reset password");
                        dialog.cancel();
                        limitLoginNumber();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        limitLoginNumber();
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void showNoticeDiaglogMessage(String message) {
        AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String hideMail(String mail) {
        String result = "";
        for(int i = 0; i < mail.split("@")[0].length(); i++) {
            if(i >= 3)
                result += "*";
            else
                result += mail.charAt(i);
        }
        return result + "@" + mail.split("@")[1];
    }

    private void limitLoginNumber() {
        countdown--;
        count++;
        if (countdown == 0) {
            wrong_info_times++;
            if (wrong_info_times >= 2) {
                addTempBannedUser();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constant.TEMP_BANNED_USER, edtUsername.getText().toString());
            } else {
                limitLoginTimer();
            }
        }
    }

    public String getUserStatus(LoginResponse loginResponse) {
        if ("Success".equals(loginResponse.getResponseCode())) {
            if ("b".equals(loginResponse.getUserModel().getBanStatus())) {
                return "banned";
            } else {
                return "success";
            }
        } else {
            if ("WUWP".equals(loginResponse.getLoginResponseStatus())) {
                return "WUWPFailed";
            } else {
                return "RUWPFailed";
            }
        }
    }

    private void limitLoginTimer() {
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
        showNoticeDiaglogMessage(Constant.DELAY_AFTER_WRONG_5_TIMES);
        for (int i = 1; i < 3; i++) {
            CountDownTimer ticktimer = new CountDownTimer(5000 * wrong_info_times, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    btnLogin.setClickable(true);
                    btnLogin.setEnabled(true);
                    countdown = 5;
                    count = 0;
                }
            };
            ticktimer.start();
        }
    }

    private void addTempBannedUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.TEMP_BANNED_USER, edtUsername.getText().toString());
        editor.apply();
    }

    private boolean isTempBannedUser() {
        String user = sharedPreferences.getString(Constant.TEMP_BANNED_USER, "null");
        return wrong_info_times >= 2 && user.equals(edtUsername.getText().toString());
    }
}