package itg8.com.nowzonedesigndemo.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import itg8.com.nowzonedesigndemo.utility.FileLogUtility;
import itg8.com.nowzonedesigndemo.utility.UserLog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // Do something
            FileLogUtility.newInstance().addLogToFile(UserLog.INTERNET_START.name());
        }else {
            FileLogUtility.newInstance().addLogToFile(UserLog.INTERNET_STOP.name());
        }
    }
}