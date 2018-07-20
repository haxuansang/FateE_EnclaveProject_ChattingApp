package application.android.com.fatee.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
>>>>>>> 3c4b3b16bf71f83df5325b74ac0fc6b63a66ad50
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
import application.android.com.fatee.utils.Constant;
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
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (no1.getText().toString().isEmpty() && no2.getText().toString().isEmpty()){
//                    Toast.makeText(RegisterActivity.this,"Done",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//            }
//        });
//        final Drawable drawable = getResources().getDrawable(R.drawable.about_icon_copy_right);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        edtRegisterConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtRegisterPassword.getText().toString().equals(edtRegisterConfirmPassword.getText().toString())){
                    edtRegisterConfirmPassword.setError(Constant.COMFIRM_ERROR,customizeErrorIcon());
                    Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
                }else edtRegisterConfirmPassword.setError(Constant.COMFIRM_ERROR);
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
//        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
//        try {
//            ColorStateList csl = ColorStateList.createFromXml(getResources(),
//                    xrp);
//
//            btnRegister.setTextColor(csl);
//        } catch (Exception e) {
//        }
    }

    public void registerSubmit(View view) {
        String username = edtRegisterUsername.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String confirmpassword = edtRegisterConfirmPassword.getText().toString();
        String nickname = edtRegisterNickname.getText().toString();
        String mail = edtRegisterEmail.getText().toString();
//        getContentfromEditText();
        if (!"".equals(username) && !"".equals(password) && !"".equals(confirmpassword) && !"".equals(nickname) && !"".equals(mail)) {
            registerPresenter.createNewAccount(new User(username, password, nickname, mail, gender));
        } else {
            showDiaglogMessage("Please complete putting your information before registering!");
        }

//        Toast.makeText(RegisterActivity.this, "Done", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
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

    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(Constant.EMAIl_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void initFocusChangeListener() {
        edtRegisterUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterUsername.hasFocus()) {
                    if (!isValidUsername(edtRegisterUsername.getText().toString())) {
                        edtRegisterUsername.setError(Constant.USERNAME_ERROR);
                    }
                }
            }
        });

        edtRegisterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterPassword.getText().toString())) {
                        edtRegisterPassword.setError(Constant.PASSWORD_ERROR);
                    }
                }
            }
        });

        edtRegisterConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterConfirmPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterConfirmPassword.getText().toString())) {
                        edtRegisterConfirmPassword.setError(Constant.PASSWORD_ERROR);
                    }
//                    else if (edtRegisterPassword.getText().toString().equals(edtRegisterPassword.getText().toString())){
//                        edtRegisterConfirmPassword.setError(Constant.COMFIRM_ERROR);
//                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
//                    }else Toast.makeText(getApplicationContext(),"NOK",Toast.LENGTH_SHORT).show();

                }
            }
        });

        edtRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterEmail.hasFocus()) {
                    if (!isValidEmail(edtRegisterEmail.getText().toString())) {
                        edtRegisterEmail.setError(Constant.EMAIL_ERROR);
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

    //    public void onRadioButtonClicked(View v) {
//        int idChecked = radiogrGender.getCheckedRadioButtonId();
//        boolean checked = ((RadioButton) v).isChecked();
//        switch (v.getId()) {
//            case R.id.radiobtn_gender_Male:
//                if(checked)
//                Toast.makeText(this,"Male",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radiobtn_gender_Female:
//                if (checked )
//                Toast.makeText(this,"Female",Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//    }
//    public boolean onRadioButtonClickedMale(View view) {
//        Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
//        return checkGender;
//    }
//
//    public boolean onRadioButtonClickedFemale(View view) {
//        Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
//        return !checkGender;
//    }
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

//    private void getContentfromEditText() {
//        String username = edtRegisterUsername.getText().toString();
//        String password = edtRegisterPassword.getText().toString();
//        String confirmPassword = edtRegisterConfirmPassword.getText().toString();
//        String nickname = edtRegisterNickname.getText().toString();
//        String email = edtRegisterEmail.getText().toString();
//    }

    @Override
    public void notificationsAfterRegisteration(RegisterResponse registerResponse) {
        Toast.makeText(this,"A",Toast.LENGTH_SHORT).show();
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
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
