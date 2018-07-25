package application.android.com.fatee.presenters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
import application.android.com.fatee.presenters.interfaces.ChattingPresenter;
import application.android.com.fatee.views.fragments.RoomFragment;

public class ChattingGroupPresenter {
    public static QBChatDialog currentQBChatDialog;
    public ChattingGroupPresenter() {
    }

    public void initConnectionTimeOut() {
        QBChatService.getInstance();
        QBChatService.setDebugEnabled(true); // enable chat logging
        QBChatService.setDefaultPacketReplyTimeout(10000);
        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(600000); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
        QBChatService.getInstance().setReconnectionAllowed(true);
    }

    public void createSessionForChat(final String quickBloxId, final QBUser qbUser, final ProgressDialog progressDialog, final FragmentTransaction fragmentTransaction) {
//        progressDialog  = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Waiting...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final ProgressDialog finalProgressDialog = progressDialog;
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
//                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                        QBChatService.getInstance().getUser().getId();
                        getQBChatDialog(quickBloxId,  progressDialog, fragmentTransaction);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error",""+e.getMessage());
//                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });

    }
    public void loadBitmapUsers() {
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUserHolder.getInstance().putUsers(qbUsers);
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });
    }

    private void getQBChatDialog(String quickBloxId, final ProgressDialog progressDialog, final FragmentTransaction fragmentTransaction) {
        QBRestChatService.getChatDialogById(quickBloxId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                currentQBChatDialog = qbChatDialog;
                progressDialog.dismiss();


                fragmentTransaction.replace(R.id.frame_layout, RoomFragment.getInstance());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }

            @Override
            public void onError(QBResponseException e) {
//                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
