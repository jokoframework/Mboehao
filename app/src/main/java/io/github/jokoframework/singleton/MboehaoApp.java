package io.github.jokoframework.singleton;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
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
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

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
            this.analytics = GoogleAnalytics.getInstance(this);
            analytics.getInstance(this).setLocalDispatchPeriod(1);
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
