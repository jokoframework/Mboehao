package io.github.jokoframework.mboehaolib.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import org.apache.commons.lang3.StringUtils;

import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.Utils;


public class CronService extends Service {
    /**
     * Log tag for this service.
     */
    private static final String LOG_TAG = "CRON";

    private IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        handlerNewsServiceExecution(); // doPeriodicTasks();
    }

    public void handlerNewsServiceExecution() {
        handler.postDelayed(runNewsService, Constants.FIRST_TIME);
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Log.i(LOG_TAG, "Entering onBind Method in CronService");
        return mBinder;
    }

    private final Handler handler = new Handler();
    Runnable runNewsService = new Runnable() {
        @Override
        public void run() {
            if (Utils.isParseSessionActive()) {
                //Sólo con una sesión válida tiene sentido correr las tareas del cron
            } else {
                Log.d(LOG_TAG, "No hay usuario autenticado");
            }
            handler.postDelayed(this, Constants.CRON_INTERVAL);
        }

    };

    public class MyBinder extends Binder {
        public CronService getService() {
            return CronService.this;
        }
    }

}
