package me.droan.netsu.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import me.droan.netsu.R;
import me.droan.netsu.tracker.TrackerActivity;

/**
 * Created by Drone on 28/09/16.
 */

public class NotificationUtil {

    static void createNotification(Context context, int type, String trackerId) {
        Intent intent = TrackerActivity.createIntent(context, trackerId);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.netsu))
                .setContentText(context.getString(R.string.reminder))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(type, builder);
    }

    public static void createBroadcast(Context context, int type, String trackerId, long timeNext) {
        Intent i = new Intent(context, NotificationReceiver.class);
        i.putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_TEMPERATURE);
        i.putExtra(NotificationReceiver.EXTRA_TRACKER_ID, trackerId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeNext, pendingIntent);
    }
}
