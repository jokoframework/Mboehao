package io.github.jokoframework.singleton;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDex;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

import io.github.jokoframework.BuildConfig;
import io.github.jokoframework.R;
import io.github.jokoframework.activity.BaseActivity;
import io.github.jokoframework.auth.AppAuthenticator;
import io.github.jokoframework.auth.AuthInterceptor;
import io.github.jokoframework.mboehaolib.service.CronService;
import io.github.jokoframework.model.UserData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


//This class just initialize the Parse Server with the URL and appID... configuration in the Android Manifest
public class MboehaoApp extends Application {

    private static final String LOG_TAG = MboehaoApp.class.getName();

    private static Context singletonApplicationContext;
    private static MboehaoApp mySelf;
    private static String hostName = "https://sodep.com.py";
    private static Boolean devHostNameDefined = false;

    /**
     * Instance variables
     */
    private UserData userData;
    private OkHttpClient.Builder httpClient;
    private Tracker mTracker;
    private GoogleAnalytics analytics;
    private BaseActivity baseActivity;

    public static MboehaoApp getApp() {
        return MboehaoApp.mySelf;
    }

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mySelf = this;
        initializeInternetServices(this);
        httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        httpClient.authenticator(new AppAuthenticator(this));
        httpClient.addInterceptor(new AuthInterceptor(this));
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logger);
        }
        MboehaoApp.setSingletonApplicationContext(this.getApplicationContext());
    }

    public static void setHostName(String newHostName){
        hostName = newHostName;
    }

    public static String getHostName(){
        return hostName;
    }

    /* Contribución basado en https://github.com/GonzaloGaleano/Mboehao */
    public static void prompDevHostName(Activity ctx) {
        if ( BuildConfig.DEBUG ) {
            if ( !devHostNameDefined ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                LayoutInflater inflater = ctx.getLayoutInflater();

                View view = inflater.inflate(R.layout.prompt_hostname_view, null);
                EditText inputHostName = view.findViewById(R.id.hostNameInput);

                builder.setView(view)
                        .setPositiveButton(R.string.button_accept, (dialog, id) -> {
                            if ( StringUtils.isBlank(inputHostName.getText())) {
                                MboehaoApp.prompDevHostName(ctx);
                                return;
                            }
                            MboehaoApp.setHostName(inputHostName.getText().toString());
                            devHostNameDefined = true;
                        })
                        .setNegativeButton(R.string.button_cancel, (dialog, id) -> {
                            if ( StringUtils.isBlank(inputHostName.getText())) {
                                MboehaoApp.prompDevHostName(ctx);
                            }
                            dialog.cancel();
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public OkHttpClient.Builder getHttpClient() {
        return httpClient;
    }

    public UserData getUserData() {
        if (userData == null) {
            userData = new UserData(PreferenceManager.getDefaultSharedPreferences(this));
        }
        return userData;
    }

    public void initializeInternetServices(Context context) {
        try {
            Intent mServiceIntent = new Intent(context, CronService.class);
            context.startService(mServiceIntent);
        } catch (RuntimeException e) {
            Toast.makeText(context, context.getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, context.getString(R.string.no_network_connection), e);
        }
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public static Context getSingletonApplicationContext() {
        return singletonApplicationContext;
    }

    public static void setSingletonApplicationContext(Context singletonApplicationContext) {
        MboehaoApp.singletonApplicationContext = singletonApplicationContext;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(0x7f070000);
            mTracker.enableAutoActivityTracking(true);

        }
        return mTracker;
    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

}
