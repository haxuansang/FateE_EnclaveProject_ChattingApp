
package application.android.com.fatee.models.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import application.android.com.fatee.utils.LoginConstant;

public class CheckTempBannedUserService extends Service {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public CheckTempBannedUserService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences = getSharedPreferences(LoginConstant.SHARED_PREFERENCES_LOCKED_USER_XML_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove(LoginConstant.TEMP_BANNED_USER_NAME);
    }
}