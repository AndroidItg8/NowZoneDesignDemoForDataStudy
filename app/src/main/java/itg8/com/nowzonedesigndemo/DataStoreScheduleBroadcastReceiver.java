package itg8.com.nowzonedesigndemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import itg8.com.nowzonedesigndemo.common.NetworkCallScheduler;

public class DataStoreScheduleBroadcastReceiver extends BroadcastReceiver {
    public static final int MY_JOB_ID = 102255;
    private static final String CUSTOM_INTENT = "itg8.com.nowzonedesigndemo.intent.action.MY_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkCallScheduler.enqueueWork(context);
    }


    public static void cancelAlarm(Context ctx) {
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        /* cancel any pending alarm */
        alarm.cancel(getPendingIntent(ctx));
    }
    public static void setAlarm(boolean force,Context ctx) {
        cancelAlarm(ctx);
        AlarmManager alarm = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        // EVERY X MINUTES
        long delay = (1000 * 60 * 1);
        long when = System.currentTimeMillis();
        if (!force) {
            when += delay;
        }

//        /* fire the broadcast */
//        alarm.set(AlarmManager.RTC_WAKEUP, when, getPendingIntent(ctx));

        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT < Build.VERSION_CODES.KITKAT)
            alarm.set(AlarmManager.RTC_WAKEUP, when, getPendingIntent(ctx));
        else if (SDK_INT < Build.VERSION_CODES.M)
            alarm.setExact(AlarmManager.RTC_WAKEUP, when, getPendingIntent(ctx));
        else {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, getPendingIntent(ctx));
        }

    }
    private static PendingIntent getPendingIntent(Context ctx) {
        Intent alarmIntent = new Intent(ctx, DataStoreScheduleBroadcastReceiver.class);
        alarmIntent.setAction(CUSTOM_INTENT);
        return PendingIntent.getBroadcast(ctx, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);


    }
}
