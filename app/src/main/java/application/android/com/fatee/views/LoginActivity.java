package application.android.com.fatee.views;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import application.android.com.fatee.R;
import application.android.com.fatee.presenters.ProcessLogicPresenter;
import application.android.com.fatee.utils.ConnectionBroadcastReceiver;
import application.android.com.fatee.views.interfaces.ViewProcessLogin;

public class LoginActivity extends AppCompatActivity implements ViewProcessLogin {
    private ImageView imageViewIcon;
    private Button btnLogin,btnRegister;
    private EditText edtUsername,edtPassword;
    private RelativeLayout relativeLayoutLogin;
    private LinearLayout linearLayoutWrongLogin;
    Handler handler;
    ProcessLogicPresenter presenterLogicProcessLogin;
    public static ViewProcessLogin viewProcessLogin;
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewProcessLogin=this;
        delayLogin();
        initView();
        presenterLogicProcessLogin = new ProcessLogicPresenter(viewProcessLogin);
        broadcastReceiver = new ConnectionBroadcastReceiver();

    }



    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);

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
}
