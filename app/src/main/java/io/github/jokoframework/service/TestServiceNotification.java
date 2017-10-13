package io.github.jokoframework.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.simplerel.R;
import com.parse.ParseUser;

import io.github.jokoframework.activity.HomeActivity;
import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.aplicationconstants.Constants;

/**
 * Created by joaquin on 03/10/17.
 */

public class TestServiceNotification extends PeriodicService {
    public static final String LOG_TAG = TestServiceNotification.class.getSimpleName();

    private Integer notificationIconId;

    public Integer getNotificationIconId() {
        return notificationIconId;
    }

    public void setNotificationIconId(Integer notificationIconId) {
        this.notificationIconId = notificationIconId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationBuilder();
        TestServiceNotification.setAlarm(this);
        return START_NOT_STICKY;
    }

    protected void notificationBuilder(){
        setNotificationIconId(R.drawable.ic_mboehao);
        String message;
        message = getString(R.string.notification_message);
        Intent dailyIntent;
        if(ParseUser.getCurrentUser() != null){
            dailyIntent = new Intent(this, HomeActivity.class);
        }else{
            dailyIntent = new Intent(this, LoginActivity.class);
        }
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
        if (context != null) {
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {
                setServiceClass(TestServiceNotification.class);
                PeriodicService.setAlarm(context,14,0,Constants.ONE_DAY,getServiceClass());
            }
        }
    }

    public static void cancelAlarm(Context context){
        PeriodicService.cancelAlarm(context,TestServiceNotification.class);
    }

}
