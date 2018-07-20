package application.android.com.fatee.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;




import application.android.com.fatee.presenters.ProcessLogicPresenterImpl;
import application.android.com.fatee.views.LoginActivity;

public class ConnectionBroadcastReceiver extends BroadcastReceiver {
    ProcessLogicPresenterImpl presenterLogicProcessLogin;

    @Override
    public void onReceive(Context context, Intent intent) {

        ProcessLogicPresenterImpl presenterLogicProcessLogin = new ProcessLogicPresenterImpl(LoginActivity.viewProcessLogin);
        if (checkConnected(context))
            presenterLogicProcessLogin.caseHaveConnection();
        else presenterLogicProcessLogin.caseLostConnection();
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