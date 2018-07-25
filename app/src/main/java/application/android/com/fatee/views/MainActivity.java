package application.android.com.fatee.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.QuickBloxResponse;
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
import application.android.com.fatee.presenters.ChattingGroupPresenter;
import application.android.com.fatee.presenters.ChattingPresenterImpl;
import application.android.com.fatee.presenters.interfaces.ChattingPresenter;
import application.android.com.fatee.utils.LoginConstant;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.views.adapters.ChatMessageAdapter;
import application.android.com.fatee.views.fragments.AboutFragment;
import application.android.com.fatee.views.fragments.NotificationsFragment;
import application.android.com.fatee.views.fragments.ProfileFragment;
import application.android.com.fatee.views.fragments.RoomFragment;
import application.android.com.fatee.views.fragments.SettingsFragment;
import application.android.com.fatee.views.fragments.BuddyFragment;
import application.android.com.fatee.views.fragments.SurveyFragment;
import application.android.com.fatee.views.interfaces.ChattingView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChattingView {
    private static FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    String username,password;
    public static  CircleImageView imageView;
    private ChattingPresenter chattingPresenter;
    private ChattingGroupPresenter chattingGroupPresenter;
    private SharedPreferences sharedPreferencesRememberedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chattingGroupPresenter = new ChattingGroupPresenter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chattingGroupPresenter.initConnectionTimeOut();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*imageView=  (CircleImageView) findViewById(R.id.imageViewUser);*/
        chattingPresenter = new ChattingPresenterImpl(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);
        String surveyStatus = getIntent().getExtras().getString(SurveyConstant.USER_SURVEY_STATUS_KEY);
        username = getIntent().getExtras().getString(LoginConstant.USERNAME);
        password = getIntent().getExtras().getString(LoginConstant.PASSWORD);

        fragmentManager = getFragmentManager();
       fragmentTransaction = fragmentManager.beginTransaction();
        if(surveyStatus.equals(SurveyConstant.USER_NO_FINISH_SURVEY_STATUS)) {
            UserUtil.getInstance(UserUtil.getUserModel(), SurveyConstant.USER_NO_FINISH_SURVEY_STATUS);
            fragmentTransaction.add(R.id.frame_layout, SurveyFragment.getInstance());
            fragmentTransaction.commit();
        } else {
            chattingPresenter.getQuickBloxIdFromServer();
        }

        fragmentManager= getFragmentManager();

    }

   /* private void getImageUser() {

        QBUser qbUser = new QBUser(username,password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

                QBContent.getFile(qbUser.getFileId()).performAsync(new QBEntityCallback<QBFile>() {
                    @Override
                    public void onSuccess(QBFile qbFile, Bundle bundle) {
                        Picasso.with(MainActivity.this).load(qbFile.getPublicUrl()).into(imageView);
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

    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        switch (id ) {
            case R.id.notifications:
                fragmentTransaction.replace(R.id.frame_layout,new NotificationsFragment());
                fragmentTransaction.commit();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        int id = item.getItemId();
        switch (id)
        {
            case R.id.room:
                fragmentTransaction.replace(R.id.frame_layout,new RoomFragment());
                fragmentTransaction.commit();

                break;
            case R.id.profile:

                fragmentTransaction.replace(R.id.frame_layout,new ProfileFragment());
                fragmentTransaction.commit();

                break;
            case R.id.survey:
                fragmentTransaction.replace(R.id.frame_layout,new SurveyFragment());
                fragmentTransaction.commit();

                break;
            case R.id.buddy:
                fragmentTransaction.replace(R.id.frame_layout,new BuddyFragment());
                fragmentTransaction.commit();

                break;
            case R.id.settings:
                fragmentTransaction.replace(R.id.frame_layout,new SettingsFragment());
                fragmentTransaction.commit();

                break;
            case R.id.about:
                Intent intentAbout = new Intent(MainActivity.this, AboutFragment.class);
                this.startActivity(intentAbout);
                break;
            case R.id.signout:
                QBUsers.signOut().performAsync(new QBEntityCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, Bundle bundle) {
                        sharedPreferencesRememberedUser = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_REMEMBERED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferencesRememberedUser.edit();
                        editor.clear();
                        editor.apply();
                        UserUtil.getInstance(null, null);
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });


                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void joinUserIntoRoom(QuickBloxResponse quickBloxResponse) {
        QBUser qbUser = new QBUser(username, password);
        chattingGroupPresenter.loadBitmapUsers();
        chattingGroupPresenter.createSessionForChat(quickBloxResponse.getQuickBloxId(), qbUser, progressDialog, fragmentTransaction);
    }
}