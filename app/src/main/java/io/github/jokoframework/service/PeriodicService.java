package io.github.jokoframework.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.preference.Preference;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by joaquin on 28/09/17.
 */

public class PeriodicService extends Service {
    public static final String LOG_TAG = PeriodicService.class.getSimpleName();

    private static Class<? extends PeriodicService> serviceClass;

    public static Class<? extends PeriodicService> getServiceClass() {
        return serviceClass;
    }

    public static void setServiceClass(Class<? extends PeriodicService> serviceClass) {
        PeriodicService.serviceClass = serviceClass;
    }

    protected long[] pattern = {0, 500, 300, 500, 300, 500};

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected static void setAlarm(Context context, int hour, int minutes,int seconds,long interval, Class<? extends PeriodicService> clazz) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, minutes);
        calendar.add(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, 0);

        // RTC_WAKEUP...
        int type = AlarmManager.RTC_WAKEUP;
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Alarm time in System.currentTimeMillis() (wall clock time in UTC), which will wake up the device when it goes off.
        long triggerAt = calendar.getTimeInMillis();
        long triggerRepeat = triggerAt * 60 * 6;

        Log.d(LOG_TAG, String.format("Alarma %s hora: %s", clazz.getSimpleName(), calendar.getTime()));

        if (Build.VERSION.SDK_INT >= 19) {
            alarmMgr.setExact(type, triggerAt, getPendingIntent(context, clazz));
        } else {
            alarmMgr.setRepeating(type, triggerRepeat, interval, getPendingIntent(context, clazz));
        }
    }

    protected static void setAlarm(Context context,int hour,int minutes,long interval,Class<? extends PeriodicService> clazz) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // RTC_WAKEUP...
        int type = AlarmManager.RTC_WAKEUP;
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAt = calendar.getTimeInMillis();

        Log.d(LOG_TAG, String.format("Alarma %s hora: %s", clazz.getSimpleName(), calendar.getTime()));

        alarmMgr.setExact(type, triggerAt, getPendingIntent(context, clazz));

    }

    protected static void cancelAlarm(Context context, Class<? extends PeriodicService> clazz) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context, clazz, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, Class<? extends PeriodicService> clazz, int flag) {
        Intent intent = new Intent(context, clazz);
        return PendingIntent.getService(context, 0, intent, flag);
    }

    private static PendingIntent getPendingIntent(Context context, Class<? extends PeriodicService> clazz) {
        return getPendingIntent(context, clazz, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
