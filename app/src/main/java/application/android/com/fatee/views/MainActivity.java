package application.android.com.fatee.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.GravityCompat;

import application.android.com.fatee.R;
import application.android.com.fatee.utils.SurveyConstant;
import application.android.com.fatee.utils.UserUtil;
import application.android.com.fatee.views.fragments.AboutFragment;
import application.android.com.fatee.views.fragments.NotificationsFragment;
import application.android.com.fatee.views.fragments.ProfileFragment;
import application.android.com.fatee.views.fragments.RoomFragment;
import application.android.com.fatee.views.fragments.SettingsFragment;
import application.android.com.fatee.views.fragments.BuddyFragment;
import application.android.com.fatee.views.fragments.SurveyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        String surveyStatus = getIntent().getExtras().getString(SurveyConstant.USER_SURVEY_STATUS_KEY);
        fragmentManager= getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        if(surveyStatus.equals(SurveyConstant.USER_NO_FINISH_SURVEY_STATUS)) {

            UserUtil.getInstance(UserUtil.getUser(), SurveyConstant.USER_NO_FINISH_SURVEY_STATUS);
            fragmentTransaction.add(R.id.frame_layout, SurveyFragment.getInstance());
        } else {
            UserUtil.getInstance(UserUtil.getUser(), SurveyConstant.USER_FINISHED_SURVEY_STATUS);
            fragmentTransaction.add(R.id.frame_layout, RoomFragment.getInstance());
        }
        fragmentTransaction.commit();
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
                UserUtil.getInstance(null, null);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}