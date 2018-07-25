package application.android.com.fatee.views.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
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

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.ProfileResponse;
import application.android.com.fatee.models.entities.UserModel;
import application.android.com.fatee.models.quickbloxholder.QBFileHolder;
import application.android.com.fatee.presenters.ProfilePresenterImpl;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.views.interfaces.ProfileView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ProfileView{
    private EditText edtNickname;
    private EditText edtEmail;
    private EditText edtDescription;
    private CircleImageView imageViewProfile;
    private Spinner dropdownGender;
    private TextView tvSave;
    private static ProfileView profileView;
    private ProfilePresenterImpl profilePresenter;
    private String[] genders;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading User Profile");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        genders = new String[] {"Male", "Female", "Other"};
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, genders);
        dropdownGender = view.findViewById(R.id.gender);
        dropdownGender.setAdapter(adapter);

        profilePresenter = new ProfilePresenterImpl(this);
        edtDescription = view.findViewById(R.id.description);
        edtEmail = view.findViewById(R.id.email);
        edtNickname = view.findViewById(R.id.nick_name);
        tvSave = view.findViewById(R.id.save);
        imageViewProfile=(CircleImageView)view.findViewById(R.id.imageview_profile);
        showUserProfile();
        setImageForUser();

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

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(pickIntent,"Select Picture"),7171);
            }
        });
        return view;
    }

    private boolean checkInputEmpty() {
        return !"".equals(edtEmail.getText().toString()) && !"".equals(edtNickname.getText().toString()) && !"".equals(edtDescription.getText().toString());
    }
    private void setImageForUser()
    {
        if(QBFileHolder.getInstance().getFileUserById(QBChatService.getInstance().getUser().getId())!=null)
        {
            imageViewProfile.setImageBitmap(QBFileHolder.getInstance().getFileUserById(QBChatService.getInstance().getUser().getId()));
            progressDialog.dismiss();
        }
        else

        {
            QBUsers.getUser(QBChatService.getInstance().getUser().getId()).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    QBContent.getFile(qbUser.getFileId()).performAsync(new QBEntityCallback<QBFile>() {
                        @Override
                        public void onSuccess(QBFile qbFile, Bundle bundle) {

                            Picasso.with(getActivity()).load(qbFile.getPublicUrl()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    QBFileHolder.getInstance().putQBFileUser(QBChatService.getInstance().getUser().getId(),bitmap);
                                    imageViewProfile.setImageBitmap(bitmap);
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });



                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });

        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==7171)
            {
                Uri selectedImage = data.getData();
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Processing");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                try {
                    InputStream in = null;
                    try {
                        in = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                    File file = new File(Environment.getExternalStorageDirectory()+"/image.png");
                    if (file.exists())
                        file.delete();
                    FileOutputStream fileOutputStream =new FileOutputStream(file);
                    fileOutputStream.write(bos.toByteArray());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    int imageSizeKb=(int)file.length()/1024;
                    if(imageSizeKb>=(1024*100))
                    {

                        return;
                    }
                    QBContent.uploadFileTask(file,true,null).performAsync(new QBEntityCallback<QBFile>() {
                        @Override
                        public void onSuccess(QBFile qbFile, Bundle bundle) {
                            int userFileID=qbFile.getId();
                            QBUser qbUser = new QBUser();
                            qbUser.setId(QBChatService.getInstance().getUser().getId());
                            qbUser.setFileId(userFileID);
                            QBUsers.updateUser(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                                @Override
                                public void onSuccess(QBUser qbUser, Bundle bundle) {
                                    progressDialog.dismiss();
                                    imageViewProfile.setImageBitmap(bitmap);
                                    Toast.makeText(getActivity(), "Update Image Successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(QBResponseException e) {

                                }
                            });
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Toast.makeText(getActivity(), "Upload Image Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}

