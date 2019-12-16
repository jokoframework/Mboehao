package io.github.jokoframework.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import io.github.jokoframework.R;
import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.Utils;

/**
 * Created by joaquin on 03/10/17.
 *
 * @author joaquin
 * @author afeltes
 */
public class TestServiceNotification extends PeriodicService {
    public static final String LOG_TAG = TestServiceNotification.class.getSimpleName();
    private static boolean alarmForSet;
    private Integer notificationIconId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationBuilder();
        return START_NOT_STICKY;
    }

    protected void notificationBuilder() {
        setNotificationIconId(R.drawable.ic_mboehao);
        String message;
        message = getString(R.string.notification_message);
        Intent dailyIntent =
                dailyIntent = new Intent(this, LoginActivity.class);

        dailyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        dailyIntent.putExtra(Constants.FROM_BACKGROUND_SERVICE, true);

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        Constants.NOTIFICATION_ID,
                        dailyIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.notification_title))
                .setContentText(message)
                .setSmallIcon(getNotificationIconId())
                .setAutoCancel(true);
        builder.setVibrate(pattern);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
    }

    public static void setAlarm(Context context) {
        setAlarmForSet(Utils.getBooleanPrefs(context, Constants.PREFERENCE_ATTRIBUTE_NOTIFICATION_CHECKED));
        if (context != null) {
        }
    }

    public static void cancelAlarm(Context context) {
        PeriodicService.cancelAlarm(context, TestServiceNotification.class);
    }

    public static boolean isAlarmForSet() {
        return alarmForSet;
    }

    public static void setAlarmForSet(boolean alarmSet) {
        alarmForSet = alarmSet;
    }


    public Integer getNotificationIconId() {
        return notificationIconId;
    }

    public void setNotificationIconId(Integer notificationIconId) {
        this.notificationIconId = notificationIconId;
    }


}
