package application.android.com.fatee.views.fragments;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.presenters.ProfilePresenterImpl;
import application.android.com.fatee.views.interfaces.ProfileView;

public class ProfileFragment extends Fragment implements ProfileView{
    private EditText no1;
    private EditText no2;
    private EditText no3;
    private Button btn;
    private static ProfileView profileView;
    private ProfilePresenterImpl profilePresenter;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        profilePresenter = new ProfilePresenterImpl(profileView);
        no1 = (EditText) view.findViewById(R.id.description);
        no2 = (EditText) view.findViewById(R.id.description);
        no3 = (EditText) view.findViewById(R.id.description);
        Button btn = (Button) view.findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = no1.getText().toString();
                String ava = no2.getText().toString();
                String nick = no3.getText().toString();
                if (!"".equals(des) && !"".equals(ava) &&!"".equals(nick)){
                    profilePresenter.updateProfile(new UserModel("U1",nick,des,ava));
                }else {
                    showDiaglogMessage("No don't do that!");
                }
            }
        });
        return view;
    }

    @Override
    public void notificationsAfterUpdate(ProfileResponse profileResponse) {
        Toast.makeText(getActivity(),"A",Toast.LENGTH_SHORT).show();
        String accountStatus = getAccountStatus(profileResponse);
        if ("success".equals(accountStatus)){
            showDiaglogMessage("You had a new account now. Let's login");
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
