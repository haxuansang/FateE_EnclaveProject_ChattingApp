package application.android.com.fatee.views;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.TextView;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.QuickBloxResponse;
import application.android.com.fatee.presenters.ChattingGroupPresenter;
import application.android.com.fatee.presenters.ChattingPresenterImpl;
import application.android.com.fatee.presenters.interfaces.ChattingPresenter;
import application.android.com.fatee.utils.LoginConstant;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.utils.UserUtil;
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
    private static CircleImageView imageViewUser;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    String username,password;

    public static CircleImageView imageView;
    private ChattingPresenter chattingPresenter;
    private ChattingGroupPresenter chattingGroupPresenter;
    private SharedPreferences sharedPreferencesRememberedUser;
    private TextView tvUserNickname;
    public static TextView tvUserDesctiption;
    public static Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chattingGroupPresenter = new ChattingGroupPresenter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chattingGroupPresenter.initConnectionTimeOut();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        chattingPresenter = new ChattingPresenterImpl(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        tvUserNickname =(TextView)header.findViewById(R.id.tv_nickname);
        tvUserNickname.setText(UserUtil.getUserModel().getNickname());
        tvUserDesctiption=(TextView)header.findViewById(R.id.tv_user_description);
        tvUserDesctiption.setText(UserUtil.getUserModel().getDescription());
        imageView=(CircleImageView)header.findViewById(R.id.imageViewUser);
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
        chattingGroupPresenter.createSessionForChat(quickBloxResponse.getQuickBloxId(), qbUser, progressDialog, fragmentTransaction,imageView,MainActivity.this);


    }


}