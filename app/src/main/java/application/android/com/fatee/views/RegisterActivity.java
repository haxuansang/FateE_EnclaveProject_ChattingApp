package application.android.com.fatee.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.RegisterResponse;
import application.android.com.fatee.models.entities.User;
import application.android.com.fatee.presenters.RegisterPresenterImpl;
import application.android.com.fatee.utils.DiaglogConstant;
import application.android.com.fatee.utils.LoginConstant;
import application.android.com.fatee.utils.MailConstant;
import application.android.com.fatee.utils.RegisterConstant;
import application.android.com.fatee.views.interfaces.RegisterView;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener, RegisterView {
    private EditText edtRegisterUsername;
    private EditText edtRegisterPassword;
    private EditText edtRegisterConfirmPassword;
    private EditText edtRegisterEmail;
    private EditText edtRegisterNickname;
    private RadioGroup radioGender;
    private Button btnRegister;
    private Button btnRegisterInLogin;
    private RadioGroup radiogrGender;
    private RadioButton radiobtnMale;
    private RadioButton radiobtnFemale;
    public static RegisterView registerView;
    boolean gender = true;
    RegisterPresenterImpl registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initView();
        initFocusChangeListener();
        registerPresenter = new RegisterPresenterImpl(this);
        edtRegisterConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtRegisterPassword.getText().toString().equals(edtRegisterConfirmPassword.getText().toString())){
                    edtRegisterConfirmPassword.setError(RegisterConstant.COMFIRM_ERROR,customizeErrorIcon());
                }else edtRegisterConfirmPassword.setError(RegisterConstant.COMFIRM_ERROR);
            }
        });

    }

    public void initView() {
        edtRegisterEmail = (EditText) findViewById(R.id.edt_register_Email);
        edtRegisterUsername = (EditText) findViewById(R.id.edt_register_Username);
        edtRegisterPassword = (EditText) findViewById(R.id.edt_register_Password);
        edtRegisterConfirmPassword = (EditText) findViewById(R.id.edt_register_PassworkVerify);
        edtRegisterNickname = (EditText) findViewById(R.id.edt_register_Nickname);
        radiogrGender = (RadioGroup) findViewById(R.id.radiogr_gender);
        radiobtnMale = (RadioButton) findViewById(R.id.radiobtn_gender_Male);
        radiobtnFemale = (RadioButton) findViewById(R.id.radiobtn_gender_Female);
        btnRegister = (Button) findViewById(R.id.btn_RegisterSubmit);
    }

    public void registerSubmit(View view) {
        String username = edtRegisterUsername.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String confirmpassword = edtRegisterConfirmPassword.getText().toString();
        String nickname = edtRegisterNickname.getText().toString();
        String mail = edtRegisterEmail.getText().toString();
        if (!"".equals(username) && !"".equals(password) && !"".equals(confirmpassword) && !"".equals(nickname) && !"".equals(mail)) {
            registerPresenter.createNewAccount(new User(username, password, nickname, mail, gender));
        } else {
            showDiaglogMessage("Please complete putting your information before registering!");
        }
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

    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(MailConstant.EMAIl_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void initFocusChangeListener() {
        edtRegisterUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterUsername.hasFocus()) {
                    if (!isValidUsername(edtRegisterUsername.getText().toString())) {
                        edtRegisterUsername.setError(LoginConstant.USERNAME_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterPassword.getText().toString())) {
                        edtRegisterPassword.setError(LoginConstant.PASSWORD_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterConfirmPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterConfirmPassword.getText().toString())) {
                        edtRegisterConfirmPassword.setError(LoginConstant.PASSWORD_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterEmail.hasFocus()) {
                    if (!isValidEmail(edtRegisterEmail.getText().toString())) {
                        edtRegisterEmail.setError(MailConstant.EMAIL_ERROR);
                    }
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            hideSoftInputKeyboard(v);
    }

    private void hideSoftInputKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radiobtn_gender_Male:
                if (checked)
                    Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.radiobtn_gender_Female:
                if (checked)
                    Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
                    gender = false;
                    break;
        }
    }


    @Override
    public void notificationsAfterRegisteration(RegisterResponse registerResponse) {
        String accountStatus = getAccountStatus(registerResponse);
            if ("success".equals(accountStatus)){
                showDiaglogMessage("You had a new account now. Let's login");
            }
            else if ("failure".equals(accountStatus)){
                showDiaglogMessage(registerResponse.getMessage());
            }
    }

    public String getAccountStatus(RegisterResponse registerResponse) {
        if ("Success".equals(registerResponse.getResponseCode())) {
            return "success";
        }
        return "failure";
    }

    private void showDiaglogMessage(String message) {
        AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(DiaglogConstant.OK_ACTION, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    public Drawable customizeErrorIcon(){
        Drawable drawable = getResources().getDrawable(R.drawable.checked);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

}
