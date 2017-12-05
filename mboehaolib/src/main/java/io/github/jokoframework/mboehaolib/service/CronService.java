package io.github.jokoframework.mboehaolib.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.ParseUtils;
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
                checkForAppNews();
            } else {
                Log.d(LOG_TAG, "No hay usuario autenticado");
            }
            handler.postDelayed(this, Constants.CRON_INTERVAL);
        }

        private void checkForAppNews() {
            ParseUser parseUser = ParseUtils.getCurrentUser();
            if (parseUser != null) {
                String appNews = ParseUtils.getParameterValue(getBaseContext(), Constants.APP_NEWS);
                if (StringUtils.isNotBlank(appNews)) {
                    Log.d(LOG_TAG, String.format("Agregando noticia de la app %s", appNews));
                    Utils.addPrefs(getBaseContext(), Constants.USER_PREFERENCE_APP_NEWS, appNews);
                    //Cada 12 horas, que corre el Cron reiniciamos sí o sí la fecha de noticias mostradas
                    Utils.addPrefs(getBaseContext(), Constants.USER_PREFERENCE_APP_NEWS_SHOWED_TIME, 0L);
                } else {
                    Log.d(LOG_TAG, String.format("Eliminanda noticia de la app"));
                }
            }
        }
    };

    public class MyBinder extends Binder {
        public CronService getService() {
            return CronService.this;
        }
    }

}
