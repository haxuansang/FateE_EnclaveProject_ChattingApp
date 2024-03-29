package application.android.com.fatee.views;

import android.app.ActivityOptions;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.utils.DiaglogConstant;
import application.android.com.fatee.utils.LoginConstant;
import application.android.com.fatee.utils.MailConstant;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.models.entities.LoginResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.models.services.CheckTempBannedUserService;
import application.android.com.fatee.presenters.ProcessLogicPresenterImpl;
import application.android.com.fatee.utils.ConnectionBroadcastReceiver;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class LoginActivity extends AppCompatActivity implements ViewProcessLogin, View.OnFocusChangeListener {
    private Button btnLogin, btnRegister;
    private EditText edtUsername, edtPassword;
    private RelativeLayout relativeLayoutLogin;
    private LinearLayout linearLayoutWrongLogin;
    private Handler handler;
    private ProcessLogicPresenterImpl presenterLogicProcessLogin;
    public static ViewProcessLogin viewProcessLogin;
    private BroadcastReceiver broadcastReceiver;
    private int countdown = 5;
    private int count = 0;
    private int wrong_info_times = 0;
    private SharedPreferences sharedPreferencesLockedUser,sharedPreferencesRememberedUser;
    private Intent service;
    private String preUsername = "";
    private ImageView imageViewIcon;
    String currentUsername,password;
    private LinearLayout loginlayout;
    private Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         QBSettings.getInstance().init(getApplicationContext(), LoginConstant.APP_ID, LoginConstant.AUTH_KEY, LoginConstant.AUTH_SECRET);
          QBSettings.getInstance().setAccountKey(LoginConstant.ACCOUNT_KEY);
        viewProcessLogin = this;
        presenterLogicProcessLogin = new ProcessLogicPresenterImpl(viewProcessLogin);
        broadcastReceiver = new ConnectionBroadcastReceiver();
        sharedPreferencesLockedUser = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_LOCKED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferencesRememberedUser = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_REMEMBERED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
        service = new Intent(LoginActivity.this, CheckTempBannedUserService.class);
        delayLogin();
        initView();

        if (checkRememberedUser()){
            relativeLayoutLogin.setVisibility(View.VISIBLE);
            loginWithoutLogin();
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            delayLogin();
            initFocusChangeListener();
        }
//        initFocusChangeListener();
//        presenterLogicProcessLogin = new ProcessLogicPresenterImpl(viewProcessLogin);
//        broadcastReceiver = new ConnectionBroadcastReceiver();
//        sharedPreferencesLockedUser = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_LOCKED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
//        sharedPreferencesRememberedUser = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_REMEMBERED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
//        service = new Intent(LoginActivity.this, CheckTempBannedUserService.class);
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
//        edtUsername.setOnFocusChangeListener(this);
//        edtPassword.setOnFocusChangeListener(this);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                login(view);
//            }
//        });

    }

    private void initView() {
        imageViewIcon = (ImageView) findViewById(R.id.imgview_icon);
        btnLogin = (Button) findViewById(R.id.btn_Login);
        btnRegister = (Button) findViewById(R.id.btn_Register);
        edtPassword = (EditText) findViewById(R.id.edt_Password);
        edtUsername = (EditText) findViewById(R.id.edt_Username);
        relativeLayoutLogin = (RelativeLayout) findViewById(R.id.relativeLayout_Login);
        linearLayoutWrongLogin = (LinearLayout) findViewById(R.id.linear_warning);
        loginlayout = (LinearLayout) findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
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
        if (!isLockedUser()) {
            String userStatus = getUserStatus(loginResponse);
            if (userStatus.equals(LoginConstant.BANNED_STATUS)) {
                showNoticeDiaglogMessage(LoginConstant.USER_BANNED_MESSAGE);
            } else if (userStatus.equals(LoginConstant.USER_LOGIN_SUCCESS_STATUS)) {
                rememberUserForTheNextLogin();
                forwardFromLoginActivityToMainActivityAfterLoginSuccessfully(loginResponse);
            } else if (userStatus.equals(LoginConstant.USER_LOGIN_WRONG_USER_AND_PASS_RETURN)) {
                showNoticeDiaglogMessage(LoginConstant.USER_LOGIN_WRONG_USER_MESSAGE);
            } else {
                countdown--;
                count++;
                if (count == 3) {
                    String loginReponseInfoMessage = loginResponse.getLoginResponseInfo().getMessage();
                    String email = loginResponse.getLoginResponseInfo().getMail();
                    showOptionDiaglogMessage(loginReponseInfoMessage, email);
                } else {
                    if (countdown > 0 && countdown <= 4) {
                        showNoticeDiaglogMessage(LoginConstant.USER_LOGIN_WRONG_PASS_MESSAGE);
                    }
                }
                limitLoginNumber();
            }
        }
    }

    private void forwardFromLoginActivityToMainActivityAfterLoginSuccessfully(LoginResponse loginResponse) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        String surveyStatus = loginResponse.getUserModel().getSurveyStatus();
        UserModel userModel = loginResponse.getUserModel();
        UserUtil.getInstance(userModel, surveyStatus);
        intent.putExtra(SurveyConstant.USER_SURVEY_STATUS_KEY, surveyStatus);
        String userId = loginResponse.getUserModel().getId();
        intent.putExtra(LoginConstant.USER_ID_MESSAGE, userId);
        intent.putExtra(LoginConstant.USERNAME,currentUsername);
        intent.putExtra(LoginConstant.PASSWORD,password);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void caseLostNetworkConnection() {
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundResource(R.drawable.disableshape);
        linearLayoutWrongLogin.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), LoginConstant.LOST_INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_SHORT);
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
                        edtPassword.setError(LoginConstant.PASSWORD_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtUsername.hasFocus()) {
                    if (!isValidUsername(edtUsername.getText().toString())) {
                        edtUsername.setError(LoginConstant.USERNAME_ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(LoginConstant.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidUsername(String username) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(LoginConstant.USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public void login(View v) {
        if (!checkValidation()) {
            loginlayout.startAnimation(shakeAnimation);
        }else {
            if (UserUtil.getUserModel() == null) {
                currentUsername = edtUsername.getText().toString();
                password = edtPassword.getText().toString();git a
                if (!preUsername.equals(currentUsername)) {
                    count = 0;
                    wrong_info_times = 0;
                    countdown = 5;
                }
                if (!currentUsername.equals(LoginConstant.USERNAME_EMPTY) && !password.equals(LoginConstant.PASSWORD_EMPTY)) {
                    User user = new User(currentUsername, md5(password));
                    UserUtil.setUser(user);
                    presenterLogicProcessLogin.getDataFromServer(user);
                    preUsername = currentUsername;
                } else {
                    showNoticeDiaglogMessage(LoginConstant.USER_INFO_REQUIREMENT_MESSAGE);
                }
            }
        }
    }



    private void showOptionDiaglogMessage(String message, final String mail) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                DiaglogConstant.YES_ACTION,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showNoticeDiaglogMessage(MailConstant.CHECK_MAIL_MESSAGE + hideMail(mail) + MailConstant.RESET_MAIL_MESSAGE);
                        dialog.cancel();
                        limitLoginNumber();
                    }
                });
        builder1.setNegativeButton(
                DiaglogConstant.NO_ACTION,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(DiaglogConstant.OK_ACTION, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String hideMail(String mail) {
        String result = "";
        for (int i = 0; i < mail.split(MailConstant.MAIL_DELIM)[0].length(); i++) {
            if (i >= 3)
                result += MailConstant.STAR_CHARACTER;
            else
                result += mail.charAt(i);
        }
        return result + MailConstant.MAIL_DELIM + mail.split(MailConstant.MAIL_DELIM)[1];
    }

    private void limitLoginNumber() {
        if (countdown == 0) {
            wrong_info_times++;
            if (wrong_info_times >= 2) {
                addLockedUser();
                SharedPreferences.Editor editor = sharedPreferencesLockedUser.edit();
                editor.putString(LoginConstant.TEMP_BANNED_USER_NAME, edtUsername.getText().toString());
            } else if (wrong_info_times < 2) {
                limitLoginTimer();
            }
        }
    }

    public String getUserStatus(LoginResponse loginResponse) {
        String responseCode = loginResponse.getResponseCode();
        if (responseCode.equals(LoginConstant.USER_LOGIN_SUCCESS_RESPONSE_CODE)) {
            String banStatus = loginResponse.getUserModel().getBanStatus();
            if (banStatus.equals(LoginConstant.BANNED_STATUS_CHARACTER)) {
                return LoginConstant.BANNED_STATUS;
            } else {
                return LoginConstant.USER_LOGIN_SUCCESS_STATUS;
            }
        } else {
            String loginResponseStatus = loginResponse.getLoginResponseStatus();
            if (loginResponseStatus.equals(LoginConstant.UER_LOGIN_WRONG_PASS_AND_USER_STATUS)) {
                return LoginConstant.USER_LOGIN_WRONG_USER_AND_PASS_RETURN;
            } else {
                return LoginConstant.USER_LOGIN_RIGHT_USER_AND_PASS_RETURN;
            }
        }
    }

    private void limitLoginTimer() {
        btnLogin.setEnabled(false);
        btnLogin.setClickable(false);
        showNoticeDiaglogMessage(LoginConstant.DELAY_AFTER_WRONG_5_TIMES_MESSAGE);
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

    private void addLockedUser() {
        SharedPreferences.Editor editor = sharedPreferencesLockedUser.edit();
        editor.putString(LoginConstant.TEMP_BANNED_USER_NAME, edtUsername.getText().toString());
        editor.apply();
    }

    private boolean isLockedUser() {
        String user = sharedPreferencesLockedUser.getString(LoginConstant.TEMP_BANNED_USER_NAME, "null");
        return wrong_info_times >= 2 && user.equals(edtUsername.getText().toString());
    }

    public void registerFromLogin(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.left_enter, R.anim.right_out);
        startActivity(intent);
    }
    private void rememberUserForTheNextLogin(){
        SharedPreferences.Editor editor = sharedPreferencesRememberedUser.edit();
        editor.clear();
        editor.putString(LoginConstant.REMEMBERED_USERNAME, edtUsername.getText().toString());
        editor.putString(LoginConstant.REMEMBERED_PASSWORD, edtPassword.getText().toString());
        editor.apply();
    }
    private boolean checkRememberedUser(){
        if (sharedPreferencesRememberedUser.getString(LoginConstant.REMEMBERED_USERNAME,"null").equals("null")) {
            return false;
        }
        else {
            return true;
        }
    }

    private void loginWithoutLogin(){
        String rememberedUsername = sharedPreferencesRememberedUser.getString(LoginConstant.REMEMBERED_USERNAME,"a");
        String rememberedPassword = sharedPreferencesRememberedUser.getString(LoginConstant.REMEMBERED_PASSWORD,"a");
        edtUsername.setText(rememberedUsername);
        edtPassword.setText(rememberedPassword);
        btnLogin.callOnClick();
        btnLogin.performClick();
    }

    public String md5(String str){
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1,digest.digest());
            result = bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Boolean checkValidation() {

        String username = edtUsername.getText().toString();
        String pwd = edtPassword.getText().toString();

        Pattern p1 = Pattern.compile(LoginConstant.USERNAME_PATTERN);
        Matcher m1 = p1.matcher(username);
        Pattern p2 = Pattern.compile(LoginConstant.PASSWORD_PATTERN);
        Matcher m2 = p2.matcher(pwd);

        if (username.equals("") || username.length() == 0
                || pwd.equals("") || pwd.length() == 0) {
            Toast.makeText(this,"You can't let the field empty",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!m1.find() || !m2.find()) {
            Toast.makeText(this,"Please check your format before submitting",Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}