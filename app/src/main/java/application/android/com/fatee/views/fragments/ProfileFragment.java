package application.android.com.fatee.views.fragments;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.presenters.ProfilePresenterImpl;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.views.interfaces.ProfileView;

public class ProfileFragment extends Fragment implements ProfileView{
    private EditText edtNickname;
    private EditText edtEmail;
    private EditText edtDescription;
//    private EditText edtGender;
    private Spinner dropdownGender;
    private TextView tvSave;
    private static ProfileView profileView;
    private ProfilePresenterImpl profilePresenter;
    private String[] genders;
    ArrayAdapter<String> adapter;
    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        genders = new String[] {"Male", "Female", "Other"};
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, genders);
        dropdownGender = view.findViewById(R.id.gender);
        dropdownGender.setAdapter(adapter);

        profilePresenter = new ProfilePresenterImpl(this);
        edtDescription = view.findViewById(R.id.description);
        edtEmail = view.findViewById(R.id.email);
        edtNickname = view.findViewById(R.id.nick_name);
        tvSave = view.findViewById(R.id.save);
        showUserProfile();
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = edtNickname.getText().toString();
                String description = edtDescription.getText().toString();
                int spinnerPosition = dropdownGender.getSelectedItemPosition();
                if (checkInputEmpty()){
                    String userId = UserUtil.getUserModel().getId();
                    UserModel userModel;
                    if(spinnerPosition == 0)
                        userModel = new UserModel(userId, nickname, description, null, true);
                    else
                        userModel = new UserModel(userId, nickname, description, null, false);
                    profilePresenter.updateProfile(userModel);

                }else {
                    showDiaglogMessage("Please fill enough information");
                }
            }
        });
        return view;
    }

    private boolean checkInputEmpty() {
        return !"".equals(edtEmail.getText().toString()) && !"".equals(edtNickname.getText().toString()) && !"".equals(edtDescription.getText().toString());
    }

    private void showUserProfile() {
        UserModel userModel = UserUtil.getUserModel();
        edtEmail.setText(userModel.getMail());
        edtNickname.setText(userModel.getNickname());
        int spinnerPosition;
        if(userModel.getGender()) {
            spinnerPosition = adapter.getPosition("Male");
        } else {
            spinnerPosition = adapter.getPosition("Female");
        }
        dropdownGender.setSelection(spinnerPosition);
        edtDescription.setText(userModel.getDescription());
    }
    @Override
    public void notificationsAfterUpdate(ProfileResponse profileResponse) {
        Toast.makeText(getActivity(),"A",Toast.LENGTH_SHORT).show();
        String accountStatus = getAccountStatus(profileResponse);
        if ("success".equals(accountStatus)){
            showDiaglogMessage("Update successfully");
        }
        else if ("failure".equals(accountStatus)){
            showDiaglogMessage(profileResponse.getMessage());
        }
    }
    public String getAccountStatus(ProfileResponse profileResponse) {
        if ("Success".equals(profileResponse.getResponseCode())) {
            return "success";
        }
        return "failure";
    }
    private void showDiaglogMessage(String message) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
