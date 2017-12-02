package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.jokoframework.R;
import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.singleton.MboehaoApp;

/**
 * Created by afeltes on 02/12/17.
 */

public class BaseActivity extends Activity {

    private static final String LOG_TAG = BaseActivity.class.getName();

    private static MboehaoApp application;
    private String appVersionName;
    protected boolean dismisableWhenNoConnection = true;
    /**
     * La primera actividad default de la apliacación. Por lo general: HomeActivity.class
     */
    private Class<? extends Activity> firstActivity;
    private boolean withInternetConnection;


    public final synchronized MboehaoApp getApp() {
        if (application == null) {
            application = (MboehaoApp) getApplicationContext();
        }
        return application;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isNetworkAvailable(this)) {
            setWithInternetConnection(true);
            //Pone la flecha de regresar atras en el action bar
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            startFristActivity(this);
        } else {
            setWithInternetConnection(false);
            Utils.showStickyMessage(this, getString(R.string.no_network_connection), Style.INFO);
            finishActivityDelayed(Constants.DEFAULT_DELAY_TO_CLOSE_INACTIVE_ACTIVITY);
        }
    }

    protected void startFristActivity(Activity activity) {
        Log.d(LOG_TAG, "Dummy");
    }

    public Class<? extends Activity> getFirstActivity() {
        return firstActivity;
    }

    public void setFirstActivity(Class<? extends Activity> firstActivity) {
        this.firstActivity = firstActivity;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    protected boolean isLandscapeMode() {
        return Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation;
    }

    protected void finishActivityDelayed(long secondsToWait) {
        new ActivityFinisherdWithDelay().execute(secondsToWait);
    }

    public void setWithInternetConnection(boolean withInternetConnection) {
        this.withInternetConnection = withInternetConnection;
    }

    public boolean isWithInternetConnection() {
        return withInternetConnection;
    }

    private class ActivityFinisherdWithDelay extends AsyncTask<Long, Void, Long> {
        @Override
        protected Long doInBackground(Long... params) {
            if (params != null && params.length > 0) {
                Utils.sleep(Constants.ONE_SECOND * params[0]);
            } else {
                Utils.sleep(Constants.ONE_SECOND * 7);
            }
            finish();
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isWithInternetConnection()) {
            setWithInternetConnection(Utils.isNetworkAvailable(this));
        }
        if (!isWithInternetConnection()) {
            showNoConnectionAndQuit();
        }
    }

    protected void showNoConnectionAndQuit() {
        final String msg = getString(R.string.no_network_connection);
        Utils.showStickyMessage(this, msg, Style.INFO);
        Toast.makeText(this, msg, Toast.LENGTH_LONG);
        Log.d(LOG_TAG, msg);
        if (isDismisableWhenNoConnection()) {
            new QuitterNoConnection().execute(msg);
        }
    }

    private class QuitterNoConnection extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Utils.sleep(Constants.ONE_SECOND * 7);
            System.exit(0);
            return null;
        }
    }

    public boolean isDismisableWhenNoConnection() {
        return dismisableWhenNoConnection;
    }

    public void setDismisableWhenNoConnection(boolean dismisableWhenNoConnection) {
        this.dismisableWhenNoConnection = dismisableWhenNoConnection;
    }
}
