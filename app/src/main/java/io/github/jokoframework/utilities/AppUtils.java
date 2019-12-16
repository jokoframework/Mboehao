package io.github.jokoframework.utilities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.jokoframework.R;
import io.github.jokoframework.activity.BaseActivity;
import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.constants.AppConstants;
import io.github.jokoframework.mboehaolib.rx.RetrofitException;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.misc.ProcessError;
import io.github.jokoframework.model.JokoLoginResponse;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.singleton.MboehaoApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

public class AppUtils {
    private static final String LOG_TAG = AppUtils.class.getName();

    private static Random random = new Random();
    public static final float EPSILON = 0.0000001f;
    private static final DateFormat monthFormatter = new SimpleDateFormat("MMMM, yyyy");
    private static final DateFormat dayFormatter = new SimpleDateFormat("EEEE");
    private static final ObjectMapper mapper = new ObjectMapper();

    private AppUtils() {
        AppUtils.random.setSeed(System.currentTimeMillis());
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


    public static void showMessageShortDuration(Activity activity, String message, Style style) {
        showMessage(activity, message, style, Configuration.DURATION_SHORT, Boolean.FALSE);
    }

    public static void showMessageLongDuration(Activity activity, String message, Style style) {
        showMessage(activity, message, style, Configuration.DURATION_LONG, Boolean.FALSE);
    }

    public static void showStickyMessage(Activity activity, String message, Style style) {
        showMessage(activity, message, style, Configuration.DURATION_INFINITE, Boolean.TRUE);
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
        //Para revisar con más cuidado, no sabemos si antes del mboehaolib se usaba el "id" para algo
        return context.getSharedPreferences("SimplePref", Context.MODE_MULTI_PROCESS);
    }

    private static SharedPreferences getSharedPreferences(Context context, String id) {
        // afeltes - 2017-01-23
        //Para revisar con más cuidado, no sabemos si antes del mboehaolib se usaba el "id" para algo
        return context.getSharedPreferences(AppConstants.SHARED_MBOEHAO_PREF, Context.MODE_MULTI_PROCESS);
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
            SharedPreferences prefs = getSharedPreferences(context, id);
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
        return String.format("mboehao-linechart-%s-%s.png", StringUtils.isBlank(suffix) ? "test" : suffix, AppUtils.getFormattedDate(new Date()));
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
        return false;
    }

    public static File getShareImagesFolder() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Mboehao/");
    }

    public static boolean isDnsWorking(String hostname) {
        boolean working = false;
        try {
            InetAddress inetAddress = InetAddress.getByName(hostname);
            working = inetAddress != null;
        } catch (UnknownHostException e1) {
            Log.e(LOG_TAG, String.format("No se puede resolver el nombre %s, %s", hostname, e1.getMessage()), e1);
        }
        return working;
    }

    //Sample method to validate and read the JWT
    public static void parseJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.decode(io.github.jokoframework.mboehaolib.constants.Constants.SECRET.getBytes(), Base64.NO_WRAP))
                .parseClaimsJws(jwt).getBody();
        Log.i(LOG_TAG, "ID: " + claims.getId());
        Log.i(LOG_TAG, "Subject: " + claims.getSubject());
        Log.i(LOG_TAG, "Issuer: " + claims.getIssuer());
        Log.i(LOG_TAG, "Expiration: " + claims.getExpiration());
    }

    public static void showError(String message, View view, View.OnClickListener action) {

        int backgroundColor = ContextCompat.getColor(view.getContext(), R.color.red);
        int fontColor = ContextCompat.getColor(view.getContext(), android.R.color.white);

        showMessage(message, view, action, backgroundColor, fontColor);
    }

    public static void showError(UserAccessResponse response, View view, View.OnClickListener action) {
        String defaultMessage = "Error: " + response.getErrorCode() + " Message: " + response.getMessage();
        String errorMesage = io.github.jokoframework.mboehaolib.constants.Constants.errors.get(response.getErrorCode());
        if (errorMesage != null) {
            showError(errorMesage, view, action);
        } else {
            showError(defaultMessage, view, action);
        }
    }

    public static void showMessage(String message, View view, View.OnClickListener action,
                                   int backgroundColor, int fontColor, int length) {
        Snackbar snackbar = Snackbar.make(view, message, length);

        snackbar.setAction("OK", action)
                .setActionTextColor(Color.WHITE);

        View snackBarView = snackbar.getView();

        snackBarView.setBackgroundColor(backgroundColor);
        TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(fontColor);

        snackbar.show();
    }

    public static void showMessage(String message, View view, View.OnClickListener action,
                                   int backgroundColor, int fontColor) {
        showMessage(message, view, action, backgroundColor, fontColor, Snackbar.LENGTH_LONG);
    }


    public static boolean networkConnectivity(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            connected = true;
        }
        return connected;
    }

    public static void hideSoftInputFromWindow(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInputFromWindow(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= io.github.jokoframework.mboehaolib.constants.Constants.PASSWORD_MIN_LENGTH;
    }

    public static boolean floatEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }


    private static void showActivityWithMessage(Context applicationContext, String intentFilter) {
        Intent errorActivity = new Intent(intentFilter);//this has to match your intent filter
        errorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 22, errorActivity, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            final String msg = String.format("No se pudo mostrar el mensaje de error de No Conexion %s", e.getMessage());
            Log.e(LOG_TAG, msg);
        }
    }

    /**
     * Muestra un simple Toast con un mensaje de error para los casos en que haya timeout
     */
    public static void showTimeOutError(Context context, SocketTimeoutException socketTimeoutException) {
        String message = "La conexión con el servidor es inestable, intente de vuelta en unos momentos";
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        Log.e(LOG_TAG, String.format(message + ". Error: %s", socketTimeoutException.getMessage(), socketTimeoutException));
    }

    /**
     * Muestra una pantalla de error genérico en caso de que haya un HttpException
     *
     * @param applicationContext El context application
     * @param httpException      La excepción Http
     */
    public static void showHttpError(Context applicationContext, HttpException httpException) {
        if (applicationContext != null) {
            String intentFilter = applicationContext.getString(R.string.show_error_activity_filter);
            showActivityWithMessage(applicationContext, intentFilter);
        } else {
            Log.e(LOG_TAG, "ApplicationContext null");
        }
    }

    public static void showNoConnectionError(Context applicationContext) throws IOException {
        if (applicationContext != null) {
            if (MboehaoApp.getApp().getBaseActivity() != null) {
                BaseActivity activity = MboehaoApp.getApp().getBaseActivity();
                Utils.showStickyMessage(activity, activity.getString(R.string.no_network_connection));
            } else {
                Log.e(LOG_TAG, "El Activity de error de conexion ya se encuentra visible.");
            }
        } else {
            Log.e(LOG_TAG, "ApplicationContext null");
        }

    }

    /**
     * Muestra la pantalla de login
     *
     * @param context   El context application
     * @param throwable
     */

    public static void showLogin(Context context, HttpException throwable) {
        if (context != null) {
            try {
                JokoLoginResponse loginResponse = readErrorResponse(throwable);
                // Esto deberia venir del backend y no deberia hacer falta,
                // por ahora seteamos un mismo mensaje aqui.
                if (loginResponse.getErrorCode().equals(JokoLoginResponse.ERROR_CODE_BAD_CREDENTIALS)) {
                    loginResponse.setMessage("Usuario y/o clave son incorrectas");
                }
                Intent i = makeLogin(context);
                i.putExtra(io.github.jokoframework.mboehaolib.constants.Constants.StartUpKeys.LOGIN_ERROR.name(), true);
                i.putExtra(io.github.jokoframework.mboehaolib.constants.Constants.StartUpKeys.LOGIN_ERROR_MSG.name(), loginResponse.getMessage());
                context.startActivity(i);
            } catch (IOException e) {
                Log.e(LOG_TAG, "No se pudo leer la respuesta del login");
            }
        }
    }

    private static JokoLoginResponse readErrorResponse(HttpException throwable) throws IOException {
        Response<?> response = throwable.response();
        ResponseBody errorBody = response.errorBody();
        JokoLoginResponse loginResponse = null;
        loginResponse = mapper.readValue(errorBody.bytes(), JokoLoginResponse.class);
        return loginResponse;
    }

    /**
     * Prepara un intent para volver a la pantalla de login
     *
     * @param context El context application
     * @return
     */
    public static Intent makeLogin(Context context) {
        Intent intent;
        intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static String formateDateAsDay(Date date) {
        return capitalize(dayFormatter.format(date));
    }

    public static String formatDateAsMonth(Date date) {
        return capitalize(monthFormatter.format(date));
    }


    public static boolean isYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        return calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR);
    }

    private static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String formatDateAsDayNumber(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Calcula el porcentaje de un valor sobre otro
     *
     * @param value valor cuyo porcentaje quiere hallarse
     * @param max   valor máximo sobre el cual se quiere hallar cierto porcentaje
     * @return el porcentaje que value es de max
     */

    public static int getPercentProgress(long value, long max) {
        if (max == 0) return 0;
        return (int) (value * 100 / max);
    }

    public static Action1<Throwable> errorHandler(ProcessError view) {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                RetrofitException error = (RetrofitException) throwable; // crashing !
                try {
                    final RetrofitException.Kind kind = error.getKind();
                    if (kind == RetrofitException.Kind.HTTP) {
                        UserAccessResponse response = error.getErrorBodyAs(UserAccessResponse.class);
                        view.afterProcessError(response);
                    } else if (kind == RetrofitException.Kind.NETWORK) {
                        if (throwable.getCause() instanceof SocketTimeoutException
                                || throwable.getCause() instanceof UnknownHostException) {
                            Log.e(LOG_TAG, error.getMessage());
                        } else {
                            view.afterProcessErrorNoConnection();
                        }
                    } else if (kind == RetrofitException.Kind.UNEXPECTED) {
                        Log.e(LOG_TAG, error.getMessage());
                        view.afterProcessError("Ha ocurrido un error inesperado");
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error " + e.getMessage(), e);
                }
            }
        };
    }

    public static long secondsToReachExpiration(long expiration) {
        long now = new Date().getTime() / 1000;
        return secondsDifference(expiration, now);
    }

    public static long secondsDifference(long expiration, long now) {

        Date expirationDate = new Date();
        //multiply the timestampt with 1000 as java expects the time in milliseconds
        expirationDate.setTime(expiration);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Log.i(LOG_TAG, "Expiración de Refresh token: " + format.format(expirationDate));

        return expiration - now;
    }

}
