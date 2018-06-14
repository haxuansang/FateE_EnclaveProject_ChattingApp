package application.android.com.fatee.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;



import application.android.com.fatee.presenters.ProcessLogicPresenter;
import application.android.com.fatee.views.LoginActivity;

public class ConnectionBroadcastReceiver extends BroadcastReceiver {
    ProcessLogicPresenter presenterLogicProcessLogin;

    @Override
    public void onReceive(Context context, Intent intent) {

        ProcessLogicPresenter presenterLogicProcessLogin = new ProcessLogicPresenter(LoginActivity.viewProcessLogin);
        if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            if (intent.hasExtra(WifiManager.EXTRA_SUPPLICANT_ERROR)) {
                 presenterLogicProcessLogin.caseLostConnection();
            }
        }
            else {
                if (!checkConnected(context))
                    presenterLogicProcessLogin.caseLostConnection();
                else presenterLogicProcessLogin.caseHaveConnection();
            }
        }
    public boolean checkConnected(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected() && networkInfo.isAvailable() && isInternetAvailable())
            return true;
        else return false;

    }
    public boolean isInternetAvailable()
    {
        try
        {
            return (Runtime.getRuntime().exec ("ping -c 1 google.com").waitFor()==0);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
}
