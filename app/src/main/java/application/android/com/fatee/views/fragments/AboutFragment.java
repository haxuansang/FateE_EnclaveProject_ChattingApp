package application.android.com.fatee.views.fragments;


import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;

import java.util.Calendar;

import application.android.com.fatee.R;
import application.android.com.fatee.models.entities.AboutPageElement;
import application.android.com.fatee.views.adapters.AboutPage;

public class AboutFragment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simulateDayNight(0);
        AboutPageElement adsElement = new AboutPageElement();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.main_logo)
                .addItem(new AboutPageElement().setTitle("Version 0.1"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("tatrunganha28@gmail.com")
                .addWebsite("http://google.com")
                .addFacebook("trung.anh.ta.1")
                .addTwitter("medyo80")
                .addYoutube("UCiC7YR-m7v6RLIqPau4d4WA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addInstagram("medyo80")
                .addGitHub("medyo")
                .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);
    }

    AboutPageElement getCopyRightsElement() {
        AboutPageElement copyRightsElement = new AboutPageElement();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
//        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
//        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
//        copyRightsElement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, copyrights, Toast.LENGTH_SHORT).show();
//            }
//        });
        return copyRightsElement;
    }

    private void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}
