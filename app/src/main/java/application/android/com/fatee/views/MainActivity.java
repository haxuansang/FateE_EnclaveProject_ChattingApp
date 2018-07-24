package application.android.com.fatee.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
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
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    ProgressDialog progressDialog;
    String username,password;
    public static  CircleImageView imageView;
    public static QBChatDialog currentQBChatDialog;
    static int FileId= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConnectionTimeOut();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*imageView=  (CircleImageView) findViewById(R.id.imageViewUser);*/

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String surveyStatus = getIntent().getExtras().getString(SurveyConstant.USER_SURVEY_STATUS_KEY);
        username = getIntent().getExtras().getString(LoginConstant.USERNAME);
        password = getIntent().getExtras().getString(LoginConstant.PASSWORD);

        createSessionForChat();
        getQBChatDialog();
        loadBitmapUsers();


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

    private void getQBChatDialog() {

        QBRestChatService.getChatDialogById("5b486a4fa28f9a13f725ebf3").performAsync(new QBEntityCallback<QBChatDialog>() {

            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                currentQBChatDialog=qbChatDialog;
                Toast.makeText(MainActivity.this, "load Group thanh cong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onError(QBResponseException e) {

                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                this.startActivity(intent);
                this.finish();
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void initConnectionTimeOut()
    {
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
    private void createSessionForChat()
    {

        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Please Waiting...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final QBUser qbUser = new QBUser(username,password);
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

                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();



                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error",""+e.getMessage());
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }
    private  void loadBitmapUsers()
    {

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



}