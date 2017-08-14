package io.github.jokoframework.utilitys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Random;


public class Utils {
    private static final String LOG_TAG = Utils.class.getName();

    private static Random random = new Random();

    private Utils() {
        Utils.random.setSeed(System.currentTimeMillis());
    }

    public static void throwToast(String msg, Context context) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String getPrefs(Context context, String id) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context);
            return prefs.getString(id, "");
        } else {
            Log.e(LOG_TAG, "Se intentó guardar una preferencia con el context null. getPrefs)");
            return "";
        }
    }

    //BEGIN-IGNORE-SONARQUBE
    private static SharedPreferences getSharedPreferences(Context context) {
        // afeltes - 2017-01-23
        //Para revisar con más cuidado, no sabemos si antes del bipolarlib se usaba el "id" para algo
        return context.getSharedPreferences("SimplePref", Context.MODE_MULTI_PROCESS);
    }
    //BEGIN-IGNORE-SONARQUBE

    public static boolean getBoolPrefs(Context context, String id) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context);
            return prefs.getBoolean(id, false);
        } else {
            Log.e(LOG_TAG, "Se intentó guardar una preferencia con el context null. getBoolPrefs)");
            return false;
        }
    }

    public static int getIntPrefs(Context context, String id) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context);
            return prefs.getInt(id, 0);
        } else {
            Log.e(LOG_TAG, "Se intentó guardar una preferencia con el context null. getInt)");
            return 0;
        }
    }

    public static long getLongPrefs(Context context, String id) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context);
            long myLongValue = 0;
            return prefs.getLong(id, myLongValue);
        } else {
            Log.e(LOG_TAG, "Se intentó guardar una preferencia con el context null. getLong)");
            return 0L;
        }
    }

    public static void addPrefs(Context context, String id, String value) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(id, value);
            edit.commit();
        } else {
            Log.e(LOG_TAG, "El Context que se desea utilizar es nulo. addPrefs(..String)");
        }
    }

    public static boolean isNetworkAvailable(Activity activity) {
        return isNetworkAvailable((Context) activity);
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isNetworkAvailable = false;
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            isNetworkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return isNetworkAvailable;
    }

}
