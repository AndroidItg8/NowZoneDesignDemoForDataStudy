package itg8.com.nowzonedesigndemo.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import itg8.com.nowzonedesigndemo.common.CommonMethod;
import itg8.com.nowzonedesigndemo.common.Prefs;
import itg8.com.nowzonedesigndemo.utility.StateCheckImp;

import static itg8.com.nowzonedesigndemo.connection.BleService.ACTION_RESET_ALL;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        final String action=intent.getAction();
        if(action.equals(Intent.ACTION_DATE_CHANGED)){
            Intent intent1=new Intent(ACTION_RESET_ALL);
            Prefs.putInt(CommonMethod.STEP_COUNT,0);
            Prefs.remove(StateCheckImp.AVG_30_MIN_CALCULATED);
            Prefs.remove(StateCheckImp.AVG_1_HR_CALCULATED);
            Prefs.remove(StateCheckImp.AVG_3_HR_CALCULATED);
            Prefs.remove(StateCheckImp.AVG_6_HR_CALCULATED);
            Prefs.remove(StateCheckImp.AVG_12_HR_CALCULATED);
            Prefs.remove(StateCheckImp.AVG_24_HR_CALCULATED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
        }

    }
}
