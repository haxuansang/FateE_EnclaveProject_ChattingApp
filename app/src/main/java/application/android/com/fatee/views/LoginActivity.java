package application.android.com.fatee.views;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.ImageView;
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
    private ImageView imageViewIcon;
    private Button btnLogin,btnRegister;
    private EditText edtUsername,edtPassword;
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
        presenterLogicProcessLogin = new ProcessLogicPresenter(viewProcessLogin);
        broadcastReceiver = new ConnectionBroadcastReceiver();
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        initFocusChangeListener();
        service = new Intent(LoginActivity.this, CheckTempBannedUserService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        edtUsername.setOnFocusChangeListener(this);
        edtPassword.setOnFocusChangeListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);

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



    //Delay animation login page
    private void delayLogin()
    {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                relativeLayoutLogin.setVisibility(View.VISIBLE);
            }
        },3000);

    }


    @Override
    public void caseLostNetworkConnection() {
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundResource(R.drawable.disableshape);
        linearLayoutWrongLogin.setVisibility(View.VISIBLE);
        Toast.makeText(this, "You lost Internet Connection. Please restart your Wifi or 3G.", Toast.LENGTH_SHORT).show();
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
        if(!hasFocus)
            hideSoftInputKeyboard(view);

    }


    //Hide soft input keyboard when tap outside edittext
    private void hideSoftInputKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    private void initFocusChangeListener() {

        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!edtPassword.hasFocus()) {
                    if (!isValidPassword(edtPassword.getText().toString())) {
                        edtPassword.setError("Your password must match and must be at least 8 and 15 maximum in length");

                    }
                }
            }

        });
        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtUsername.hasFocus()) {
                    if (!isValidUsername(edtUsername.getText().toString())) {
                        edtUsername.setError("Your username must match and must be at least 8 and 15 maximum in length");

                    }
                }
            }
        });

    }
    public boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#?!@$%^&*-])[A-Za-z\\d#?!@$%^&*-]{8,15}";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isValidUsername(String username) {

        Pattern pattern;
        Matcher matcher;
        final String USERNAME_PATTERN = "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d#?!@$%^&*-]{8,15}";

        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(username);

        return matcher.matches();

    }
    public void login(View v) {

        checkTempBannedUser();

        if (edtUsername.getText().toString().equals("admin123456") && edtPassword.getText().toString().equals("Identa4590!")) {
            Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
        }  else if (edtUsername.getText().toString().equals("admin123456") && !edtPassword.getText().toString().equals("Identa4590!")){

            countdown--;
            count++;

            if (countdown == 4 || countdown == 3) {
                Toast.makeText(getApplicationContext(), "You put a wrong password or username, try again", Toast.LENGTH_SHORT).show();
            }
            if (countdown > 0 && countdown < 3) {
                Toast.makeText(getApplicationContext(), "You put a wrong password " + count + " times, you just have " + countdown + " times before dalaying the process", Toast.LENGTH_SHORT).show();
            }
            if (countdown == 0) {
                wrong_info_times++;
                if (wrong_info_times >= 2) {

                    addTempBannedUser();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constant.TEMP_BANNED_USER,edtUsername.getText().toString());
                    startService(service);
                    CountDownTimer ticktimer = new CountDownTimer(5000 , 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {


                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                            stopService(service);
                        }

                    };
                    ticktimer.start();
                    wrong_info_times =0;
                    login(v);
                }
                else  {
                    btnLogin.setEnabled(false);
                    btnLogin.setClickable(false);
                    Toast.makeText(getApplicationContext(), "You put a wrong password " + count + " times, now the login process is delayed", Toast.LENGTH_SHORT).show();
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
    public void addTempBannedUser(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.TEMP_BANNED_USER,edtUsername.getText().toString());

        editor.apply();

    }
    public void checkTempBannedUser(){

        String user = sharedPreferences.getString(Constant.TEMP_BANNED_USER,"null");
        if (user.equals(edtUsername.getText().toString())){
            Toast.makeText(getApplicationContext(),"You can't login with this username anymore ",Toast.LENGTH_SHORT).show();

        }

    }
    public void startService(View view){
        startService(new Intent(getBaseContext(),CheckTempBannedUserService.class));
    }
    public void stopService(View view){
        stopService(new Intent(getBaseContext(),CheckTempBannedUserService.class));
    }
}
