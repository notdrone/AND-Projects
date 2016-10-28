package me.droan.netsu.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Drone on 28/09/16.
 */

public class NotificationReceiver extends BroadcastReceiver {

    public static final String EXTRA_TYPE = "me.sroan.netsu.notification.type";
    public static final String EXTRA_TRACKER_ID = "me.sroan.netsu.notification.trackerId";
    public static final int TYPE_TEMPERATURE = 907;
    private static final int TYPE_MEDICINE = 876;
    private static final String TAG = "@@#@$#$@#$@#$@@$@#$";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        int type = bundle.getInt(EXTRA_TYPE, -1);
        String trackerId = bundle.getString(EXTRA_TRACKER_ID);
        if (type == -1) {
            return;
        } else if (type == TYPE_TEMPERATURE) {
            NotificationUtil.createNotification(context, type, trackerId);
        } else if (type == TYPE_MEDICINE) {

        }
    }
}
