package io.github.jokoframework.utilitys;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import io.github.jokoframework.aplicationconstants.Constants;


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

    public static void showMessageLongDuration(Activity activity, String message, Style style) {
        showMessage(activity, message, style, Configuration.DURATION_LONG, Boolean.FALSE);
    }

    public static void showMessage(Activity activity, String message, Style style, Integer duration, Boolean sticky) {
        final Configuration configuration = new Configuration.Builder()
                .setDuration(duration)
                .build();
        final Crouton crouton = Crouton.makeText(activity, message, style)
                .setConfiguration(configuration);
        if (sticky) {
            crouton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crouton.hide(crouton);
                }
            });
        }
        crouton.show();
    }

    //BEGIN-IGNORE-SONARQUBE
    private static SharedPreferences getSharedPreferences(Context context) {
        // afeltes - 2017-01-23
        //Para revisar con más cuidado, no sabemos si antes del bipolarlib se usaba el "id" para algo
        return context.getSharedPreferences("SimplePref", Context.MODE_MULTI_PROCESS);
    }

    private static SharedPreferences getSharedPreferences(Context context, String id, boolean getter) {
        // afeltes - 2017-01-23
        //Para revisar con más cuidado, no sabemos si antes del bipolarlib se usaba el "id" para algo
        return context.getSharedPreferences(Constants.SHARED_MBOEHAO_PREF, Context.MODE_MULTI_PROCESS);
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

    public static void addPrefs(Context context, String id, long value) {
        if (context != null) {
            SharedPreferences prefs = getSharedPreferences(context, id, false);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putLong(id, value);
            edit.commit();
        } else {
            Log.e(LOG_TAG, "Se intentó guardar una preferencia con el context null. addPrefs(... long)");
        }
    }


    public static boolean isNetworkAvailable(Activity activity) {
        return isNetworkAvailable((Context) activity);
    }

    public static String getShareableImageName(String suffix) {
        return String.format("mboehao-linechart-%s-%s.png", StringUtils.isBlank(suffix) ? "test" : suffix, Utils.getFormattedDate(new Date()));
    }

    public static String getFormattedDate(Date savedAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        return sdf.format(savedAt);
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

    public static boolean isParseSessionActive() {
        return ParseUtils.getCurrentUser() != null && ParseUtils.getCurrentUser().isAuthenticated();
    }

    public static File getShareImagesFolder() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Mboehao/");
    }


    public static boolean isValidTime(String exactTime) {

        //TODO: utilizar un DateFormat http://javatechniques.com/blog/dateformat-and-simpledateformat-examples/
        if (StringUtils.isBlank(exactTime)) {
            return false;
        } else {
            try {
                return valideTimeAsString(exactTime);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                return false;
            } catch (ArrayIndexOutOfBoundsException arrayEx) {
                Log.e(LOG_TAG, arrayEx.getMessage(), arrayEx);
                return false;
            }
        }
    }

    protected static boolean valideTimeAsString(String exactTime) {
        String[] parts = exactTime.split(":");
        int hora = Integer.parseInt(parts[0]);
        int minuto = Integer.parseInt(parts[1]);
        if (isValidHour(hora) && isValidMinute(minuto)) {
            return true;
        }
        return false;
    }

    private static boolean isValidMinute(int minuto) {
        return minuto >= 0 && minuto <= 59;
    }

    private static boolean isValidHour(int hora) {
        return hora >= 0 && hora <= 23;
    }

}
