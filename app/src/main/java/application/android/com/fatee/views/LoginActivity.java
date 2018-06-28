package application.android.com.fatee.views;

import android.content.BroadcastReceiver;
<<<<<<< HEAD
=======
import android.content.Context;
>>>>>>> 0014243_replace_fake_data_by_data_from_server_login_function
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.android.com.fatee.R;
import application.android.com.fatee.models.services.CheckTempBannedUserService;
import application.android.com.fatee.presenters.ProcessLogicPresenter;
import application.android.com.fatee.utils.ConnectionBroadcastReceiver;
import application.android.com.fatee.utils.Constant;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class LoginActivity extends AppCompatActivity implements ViewProcessLogin, View.OnFocusChangeListener {
    private Button btnLogin, btnRegister;
    private EditText edtUsername, edtPassword;
    private RelativeLayout relativeLayoutLogin;
    private LinearLayout linearLayoutWrongLogin;
    Handler handler;
    ProcessLogicPresenter presenterLogicProcessLogin;
    public static ViewProcessLogin viewProcessLogin;
    BroadcastReceiver broadcastReceiver;
    private int countdown = 5;
    private int count = 0;
    private int wrong_info_times = 0;
    SharedPreferences sharedPreferences;
    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewProcessLogin = this;
        delayLogin();
        initView();
        initFocusChangeListener();
        presenterLogicProcessLogin = new ProcessLogicPresenter(viewProcessLogin);
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
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    // Init app View
    private void initView()
    {
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

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_Login);
        btnRegister = (Button) findViewById(R.id.btn_Register);
        edtPassword = (EditText) findViewById(R.id.edt_Password);
        edtUsername = (EditText) findViewById(R.id.edt_Username);
        relativeLayoutLogin = (RelativeLayout) findViewById(R.id.relativeLayout_Login);
        linearLayoutWrongLogin = (LinearLayout) findViewById(R.id.linear_warning);
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
    public void caseLostNetworkConnection() {
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundResource(R.drawable.disableshape);
        linearLayoutWrongLogin.setVisibility(View.VISIBLE);
        showNotice(Constant.LOST_INTERNET_CONNECTION, Toast.LENGTH_SHORT);
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
                        showError(edtPassword, Constant.PASSWORD_ERROR);
                    }
                }
            }
        });

        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtUsername.hasFocus()) {
                    if (!isValidUsername(edtUsername.getText().toString())) {
                        showError(edtUsername, Constant.USERNAME_ERROR);
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

        checkTempBannedUser();

        if (edtUsername.getText().toString().equals("admin123456") && edtPassword.getText().toString().equals("Identa4590!")) {
            showNotice(Constant.LOGIN_SUCCESS, Toast.LENGTH_SHORT);
        } else if (edtUsername.getText().toString().equals("admin123456") && !edtPassword.getText().toString().equals("Identa4590!")) {
            countdown--;
            count++;
            if (countdown > 0 && countdown <= 4) {
                showNotice(Constant.RIGHT_USERNAME_WRONG_PASSWORD, Toast.LENGTH_LONG);
            }
            if (countdown == 0) {
                wrong_info_times++;
                if (wrong_info_times >= 2) {

                    addTempBannedUser();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constant.TEMP_BANNED_USER, edtUsername.getText().toString());

                } else {

                    btnLogin.setEnabled(false);
                    btnLogin.setClickable(false);
                    showNotice(Constant.DELAY_AFTER_WRONG_5_TIMES, Toast.LENGTH_LONG);
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

            }

        }
    }

    public void addTempBannedUser() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.TEMP_BANNED_USER, edtUsername.getText().toString());
        editor.apply();

    }

    public void checkTempBannedUser() {

        String user = sharedPreferences.getString(Constant.TEMP_BANNED_USER, "null");
        if (wrong_info_times >= 2) {
            if (user.equals(edtUsername.getText().toString())) {
                showNotice(Constant.LOCKED_USERNAME, Toast.LENGTH_SHORT);
            }
        }
    }

    public void startService(View view) {
        startService(new Intent(getBaseContext(), CheckTempBannedUserService.class));
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), CheckTempBannedUserService.class));
    }

    private void showNotice(String text, int type) {
        Toast.makeText(getApplicationContext(), text, type).show();
    }

    private void showError(EditText editText, String error) {
        editText.setError(error);
    }

}
