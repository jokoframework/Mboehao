package io.github.jokoframework.misc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.widget.Toast;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.activity.SplashActivity;
import io.github.jokoframework.mboehaolib.R;
import io.github.jokoframework.mboehaolib.model.DefaultResponse;
import io.github.jokoframework.model.*;
import io.github.jokoframework.mboehaolib.rx.RetrofitException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import io.github.jokoframework.mboehaolib.constants.Constants;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * Created by joaquin on 26/10/17.
 */

public class Utilitys {

    public static final String TAG = Utilitys.class.getSimpleName();


    public static boolean NO_CONEXION_VISIBLE = false;
    public static final float EPSILON = 0.0000001f;
    public static final String DOTS = "...";

    private static final DateFormat monthFormatter = new SimpleDateFormat("MMMM, yyyy");
    private static final DateFormat dayFormatter = new SimpleDateFormat("EEEE");

    private static final ObjectMapper mapper = new ObjectMapper();


    public static void setVisibility(ViewGroup layout, int flag) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                child.setVisibility(flag);
                setVisibility((ViewGroup) child, flag);
            } else {
                child.setVisibility(flag);
            }
        }
    }

    public static Intent createIntentNoConnection(Activity activity) {
        Intent intent;
        intent = new Intent(activity, SplashActivity.class);
        return intent;
    }

    public static String getTimeFromDate(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm", Constants.LOCALE);
        df.setTimeZone(Constants.TIME_ZONE);

        return df.format(date);
    }

    public static boolean isDnsWorking(String hostname) {
        boolean working = false;
        try {
            InetAddress i = InetAddress.getByName(hostname);
            working = true;
        } catch (UnknownHostException e1) {
            Log.e(TAG, String.format("No se puede resolver el nombre %s, %s", hostname, e1.getMessage()), e1);
        }
        return working;
    }

    public static String getDateStringToRequest(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Constants.LOCALE);
        df.setTimeZone(Constants.TIME_ZONE);

        return df.format(calendar.getTime());
    }

    public static Date parseDate(String dateString){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Constants.LOCALE);
        Date date;
        try {
            if (dateString == null) throw new NullPointerException();

            date = df.parse(dateString);
        } catch (NullPointerException | ParseException e){
            date = Calendar.getInstance().getTime();
        }
        return date;
    }

    public static String reformatDate(SimpleDateFormat simpleDateFormat, String dateString) {
        Date date;
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (simpleDateFormat == null) {
                throw new NullPointerException();
            }
            date = iso8601Format.parse(dateString);
        } catch (NullPointerException | ParseException e){
            date = Calendar.getInstance().getTime();
        }
        return simpleDateFormat.format(date);
    }

    public static String getDateString(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm", Constants.LOCALE);
        return df.format(calendar.getTime());
    }

    public static String getDateStringToField(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE);
        df.setTimeZone(Constants.TIME_ZONE);

        return df.format(calendar.getTime());
    }

    public static String formatPhoneNumberWithoutZero(String phoneNumber) {
        String formatted = phoneNumber;
        if (phoneNumber.length() > 2 && phoneNumber.startsWith("0")) {
            formatted = phoneNumber.substring(1, phoneNumber.length());
        }
        return formatted;
    }

    public static boolean atLeastOneAlpha(String string) {
        return string.matches(".*[a-zA-Z]+.*|.*\\p{Punct}+.*");
    }

    public static Number parseNumber(String montoString) {
        NumberFormat format = NumberFormat.getInstance(Constants.LOCALE);
        Number number = null;
        try {
            number = format.parse(montoString);
        } catch (ParseException e) {
            Log.i(TAG, "No es número: " + montoString);
        }
        return number;
    }

    public static Long parseLong(String montoString) {
        Number number = parseNumber(montoString);
        Long monto = null;

        if (number != null) {
            monto = number.longValue();
        }

        return monto;
    }

    public static Double parseDouble(String montoString) {
        Number number = parseNumber(montoString);
        Double monto = null;

        if (number != null) {
            monto = number.doubleValue();
        }

        return monto;
    }

    public static String getFullDate(Date date) {
        DateFormat df = new SimpleDateFormat("'El' EEEE, dd MMMM yyyy 'a las' HH:mm:ss", Constants.LOCALE);
        df.setTimeZone(Constants.TIME_ZONE);

        return df.format(date);
    }

    public static String getShortDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM", Constants.LOCALE); // 3-letter month name & 2-char day of month

        return formatter.format(date);
    }

    /**
     * Retorna una fecha formateada con un estilo compacto en dos líneas
     * @param date
     * @return
     */
    public static String getCompactDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm EEE dd\nMMM yyyy", Constants.LOCALE);

        return formatter.format(date).toUpperCase();
    }

    /**
     * Retorna la fecha (sin año) y la hora, el formato que se intenta conseguir es como sigue:
     * "6:40 PM Lunes 17": El nombre del mes debe ser completo y capitalizado, la hora debe ir
     * en formato AM/PM en mayúsuculas sin los puntos de la abreviatura.
     * @param date
     * @return
     */
    public static String getDayAndTime(Date date){
        SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE dd", Constants.LOCALE);
        String day = dayFormatter.format(date);
        String finalDay = Utilitys.capitalize(day);
        String hour = new SimpleDateFormat(" h:mm a", Constants.LOCALE).format(date).toUpperCase().replace(".", "");
        return finalDay + hour;
    }

    //Sample method to validate and read the JWT
    public static void parseJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.decode(Constants.SECRET.getBytes(), Base64.NO_WRAP))
                .parseClaimsJws(jwt).getBody();
        Log.i(TAG, "ID: " + claims.getId());
        Log.i(TAG, "Subject: " + claims.getSubject());
        Log.i(TAG, "Issuer: " + claims.getIssuer());
        Log.i(TAG, "Expiration: " + claims.getExpiration());
    }

    public static long secondsToReachExpiration(long expiration) {
        long now = new Date().getTime() / 1000;
        return secondsDifference(expiration, now);
    }

    public static long secondsDifference(long expiration, long now) {

        Date expirationDate = new Date();
        //multiply the timestampt with 1000 as java expects the time in milliseconds
        expirationDate.setTime(expiration);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Constants.LOCALE);
        Log.i(TAG, "Expiración de Refresh token: " + format.format(expirationDate));

        return expiration - now;
    }

    public static void showError(String message, View view, View.OnClickListener action) {

        int backgroundColor = ContextCompat.getColor(view.getContext(), R.color.red);
        int fontColor = ContextCompat.getColor(view.getContext(), android.R.color.white);

        showMessage(message, view, action, backgroundColor, fontColor);
    }

    public static void showError(UserAccessResponse response, View view, View.OnClickListener action) {
        String defaultMessage = "Error: " + response.getErrorCode() + " Message: " + response.getMessage();
        String errorMesage = Constants.errors.get(response.getErrorCode());
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
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(fontColor);

        snackbar.show();
    }

    public static void showMessage(String message, View view, View.OnClickListener action,
                                   int backgroundColor, int fontColor) {
        showMessage(message, view, action, backgroundColor, fontColor, Snackbar.LENGTH_LONG);
    }


    public static boolean networkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
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
        return password.length() >= Constants.PASSWORD_MIN_LENGTH;
    }

    public static boolean floatEquals(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }

    public static String formatCurrency(Number number, boolean addSimbol, int decimals) {

        String quantityOut;
        if (number == null) {
            number = 0;
        }
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Constants.LOCALE);
        numberFormatter.setMaximumFractionDigits(decimals);
        numberFormatter.setMinimumFractionDigits(decimals);
        if (addSimbol) {
            quantityOut = Constants.CURRENCY_SYMBOL + numberFormatter.format(number);
        } else {
            quantityOut = numberFormatter.format(number);
        }

        return quantityOut;
    }

    private static void showActivityWithMessage(Context applicationContext, String intentFilter) {
        Intent errorActivity = new Intent(intentFilter);//this has to match your intent filter
        errorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 22, errorActivity, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            final String msg = String.format("No se pudo mostrar el mensaje de error de No Conexion %s", e.getMessage());
            Log.e(TAG, msg);
        }
    }
    /**
     * Muestra un simple Toast con un mensaje de error para los casos en que haya timeout
     */
    public static void showTimeOutError(Context context, SocketTimeoutException socketTimeoutException){
        String message = "La conexión con el servidor es inestable, intente de vuelta en unos momentos";
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, String.format(message + ". Error: %s", socketTimeoutException.getMessage(), socketTimeoutException));
    }

    /**
     * Muestra una pantalla de error genérico en caso de que haya un HttpException
     *
     * @param applicationContext El context application
     * @param httpException La excepción Http
     */
    public static void showHttpError(Context applicationContext, HttpException httpException) {
        if(applicationContext != null) {
            String intentFilter = applicationContext.getString(R.string.show_error_activity_filter);
            showActivityWithMessage(applicationContext, intentFilter);
        } else {
            Log.e(TAG, "ApplicationContext null");
        }
    }

    public static void showNoConnectionError(Context applicationContext) throws IOException {
        if(applicationContext != null) {
            if (!NO_CONEXION_VISIBLE) {
                NO_CONEXION_VISIBLE = true;
                String intentFilter = applicationContext.getString(R.string.error_activity_filter);
                showActivityWithMessage(applicationContext, intentFilter);
            } else {
                Log.e(TAG, "El Activity de error de conexion ya se encuentra visible.");
            }
        } else {
            Log.e(TAG, "ApplicationContext null");
        }
    }

    /**
     * Muestra la pantalla de login
     *
     * @param context El context application
     * @param throwable
     */

    public static void showLogin(Context context, HttpException throwable) {
        if (context != null) {
            JokoLoginResponse loginResponse = readErrorResponse(throwable);

            // Esto deberia venir del backend y no deberia hacer falta,
            // por ahora seteamos un mismo mensaje aqui.
            if (loginResponse.getErrorCode().equals(JokoLoginResponse.ERROR_CODE_BAD_CREDENTIALS)) {
                loginResponse.setMessage("Usuario y/o clave son incorrectas");
            }

            Intent i = Utilitys.makeLogin(context);
            i.putExtra(Constants.StartUpKeys.LOGIN_ERROR.name(), true);
            i.putExtra(Constants.StartUpKeys.LOGIN_ERROR_MSG.name(), loginResponse.getMessage());
            context.startActivity(i);
        }
    }

    private static JokoLoginResponse readErrorResponse(HttpException throwable) {
        Response<?> response = throwable.response();
        ResponseBody errorBody = response.errorBody();
        JokoLoginResponse loginResponse = null;
        try {
            loginResponse = mapper.readValue(errorBody.bytes(), JokoLoginResponse.class);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
            throw new RuntimeException(ex);
        }

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
     * @param value valor cuyo porcentaje quiere hallarse
     * @param max valor máximo sobre el cual se quiere hallar cierto porcentaje
     * @return el porcentaje que value es de max
     */

    public static int getPercentProgress(long value, long max){
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
                            Log.e(TAG, error.getMessage());
                        } else {
                            view.afterProcessErrorNoConnection();
                        }
                    } else if (kind == RetrofitException.Kind.UNEXPECTED) {
                        Log.e(TAG, error.getMessage());
                        view.afterProcessError("Ha ocurrido un error inesperado");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error " + e.getMessage(), e);
                }
            }
        };
    }
}
