package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.net.SocketTimeoutException;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.constants.Constants.StartUpKeys;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.repository.LoginRepository;
import io.github.jokoframework.repository.RepoBuilder;
import io.github.jokoframework.utilities.AppUtils;
import retrofit2.Response;


public class SplashActivity extends BaseActivity {

    public static final String LOG_TAG = SplashActivity.class.getSimpleName();
    private UserData userData;
    private LoginRepository api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        showProgress(true, "Inicializando la aplicación...");
        if (isWithInternetConnection()) {
            api = RepoBuilder.getSyncApi(LoginRepository.class);
            userData = getUserData();
            initializeFirstActivity();
        }
    }

    public void checkConnection() {
        if (!AppUtils.networkConnectivity(getApplicationContext())) {
            showNoConnectionAndQuit();
        } else {
            initApp();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnection();
    }

    private void initializeFirstActivity() {
        int splashTimeOut = 3000;
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, splashTimeOut);
    }

    private void initApp() {
        new CheckUserDataTask(this).execute();
    }

    private boolean setAccessToken() {
        String refreshToken = userData.getRefreshToken();
        UserAccessResponse response;
        boolean result = false;
        try {
            Response<UserAccessResponse> httpResponse = api.refreshUserAccess(refreshToken).execute();
            Log.i(LOG_TAG, "Response raw: " + httpResponse.raw());
            if (httpResponse.isSuccessful()) {
                setWithInternetConnection(true);
                response = httpResponse.body();
                if (response.getSuccess()) {
                    Log.i(LOG_TAG, "Refresh AccessToken success");
                    result = true;
                    userData.setUserAccessToken(response.getSecret());
                } else {
                    Log.i(LOG_TAG, "Refresh AccessToken fail 200");
                    userData.logout();
                }
            } else {
                Log.i(LOG_TAG, "Refresh AccessToken fail not 200");
                userData.logout();
            }
        } catch (SocketTimeoutException e) {
            Log.e(LOG_TAG, "---------------Error al pedir Access Token SSL handshake timed out \n" + e.getMessage(), e);
            finish();
        } catch (Exception e) {
            Log.e(LOG_TAG, "---------------Error al pedir Access Token \n" + e.getMessage(), e);
        }
        return result;
    }

    private Constants.StartUpKeys checkRefreshToken() {
        Constants.StartUpKeys result = StartUpKeys.NOT_LOGGED;
        if (userData.isLogged()) {
            Log.i(LOG_TAG, "Refresh token:" + userData.getRefreshToken());
            long expiration = userData.getExpiration();

            long secondsToReachExpiration = AppUtils.secondsToReachExpiration(expiration);

            if (secondsToReachExpiration < Constants.SECONDS_TO_RESET) {
                result = StartUpKeys.EXPIRED;
                userData.logout();
            } else if ((secondsToReachExpiration < Constants.SECONDS_TO_REFRESH
                    && secondsToReachExpiration > Constants.SECONDS_TO_RESET)
                    && setRefeshToken()) {
                result = StartUpKeys.LOGGED;
            } else {
                if (secondsToReachExpiration > Constants.SECONDS_TO_REFRESH
                        && setAccessToken()) {
                    result = StartUpKeys.LOGGED;
                }
            }
        }

        return result;
    }

    private boolean setRefeshToken() {
        String refreshToken = userData.getRefreshToken();
        UserAccessResponse response;
        boolean result = false;
        try {
            Response<UserAccessResponse> httpResponse = api.refreshToken(refreshToken).execute();
            Log.i(LOG_TAG, "Response raw: " + httpResponse.raw());
            response = httpResponse.body();
            if (response.getSuccess()) {
                setWithInternetConnection(true);
                Log.i(LOG_TAG, "Refresh RefreshToken success");
                Log.i(LOG_TAG, "New refresh token: " + response.getSecret());
                userData.setRefreshToken(response.getSecret());
                userData.setExpiration(response.getExpiration());
                userData.persistUser();
                result = setAccessToken();
            } else {
                Log.i(LOG_TAG, "Refresh RefreshToken fail");
                Log.i(LOG_TAG, "ERROR: " + response.getErrorCode());
                userData.logout();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "---------------Error al pedir refrescar el refresh Token \n" + e.getMessage(), e);
            getUserData().logout();
        }
        return result;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class CheckUserDataTask extends AsyncTask<Void, Void, StartUpKeys> {
        private Activity activity;

        public CheckUserDataTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected StartUpKeys doInBackground(Void... params) {
            return SplashActivity.this.checkRefreshToken();
        }

        @Override
        protected void onPostExecute(final StartUpKeys result) {
            Intent intent = null;

            switch (result) {
                case LOGGED:
                    intent = new Intent(activity, HomeActivity.class);
                    break;
                case NOT_LOGGED:
                    intent = new Intent(activity, LoginActivity.class);
                    break;
                case EXPIRED:
                    intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra(StartUpKeys.EXPIRED.name(), true);
                    break;
                default:
                    Log.w(LOG_TAG, getString(R.string.defaultSwitchMessage));
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            activity.finish();
        }
    }

}
