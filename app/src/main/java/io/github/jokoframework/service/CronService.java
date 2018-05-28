package io.github.jokoframework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import io.github.jokoframework.R;
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
        handlerRESTServiceExecution(); // doPeriodicTasks();
    }

    public void handlerRESTServiceExecution() {
        handler.post(runRESTService);
    }

    @Override
    public IBinder onBind(final Intent intent) {
        Log.i(LOG_TAG, "Entering onBind Method in CronService");
        return mBinder;
    }

    private final Handler handler = new Handler();
    Runnable runRESTService = new Runnable() {
        @Override
        public void run() {
            checkAPI();
            handler.postDelayed(this, Constants.ONE_HOUR);
        }

        private void checkAPI() {
            try {
                Intent mServiceIntent = new Intent(getBaseContext(), io.github.jokoframework.service.CountryHelper.class);
                getBaseContext().startService(mServiceIntent);
            } catch (RuntimeException e) {
                Utils.showToast(getBaseContext(), String.format("Fallo de CountryHelper"));
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, getBaseContext().getString(R.string.no_network_connection), e);
            }
        }
    };

    public class MyBinder extends Binder {
        public CronService getService() {
            return CronService.this;
        }
    }

}
