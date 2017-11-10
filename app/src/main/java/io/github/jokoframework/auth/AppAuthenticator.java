package io.github.jokoframework.auth;

import android.util.Log;

import java.io.IOException;

import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.repository.LoginRepository;
import io.github.jokoframework.repository.RepoBuilder;
import io.github.jokoframework.singleton.MboehaoApp;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author nahuel
 */

public class AppAuthenticator implements Authenticator {
    private static final String LOG_TAG = AppAuthenticator.class.getSimpleName();
    private MboehaoApp application;
    private LoginRepository api;

    public AppAuthenticator(MboehaoApp application) {
        this.application = application;
        api = RepoBuilder.getSyncApi(LoginRepository.class);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (responseCount(response) >= 3) {
            return null; // If we've failed 3 times, give up.
        }

        String credential = getNewToken();
        if (credential == null) {
            return  null;
        }

        return response.request().newBuilder()
                .header(Constants.HEADER_AUTH, credential)
                .build();
    }

    private int responseCount(Response original) {
        int result = 1;
        Response response = original;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    private String getNewToken() {
        UserAccessResponse response;
        try {
            UserData userData = application.getUserData();
            retrofit2.Response<UserAccessResponse> httpResponse = api.refreshUserAccess(userData.getRefreshToken())
                    .execute();
            if (httpResponse.isSuccessful()) {
                response = httpResponse.body();
                if (response.getSuccess()) {
                    Log.i(LOG_TAG, "Refresh access token success");
                    userData.setUserAccessToken(response.getSecret());
                    Log.i(LOG_TAG, "New access token: " + response.getSecret());
                    return response.getSecret();
                } else {
                    Log.i(LOG_TAG, "Response: " + response.getMessage());
                    return null;
                }
            } else {
                Log.i(LOG_TAG, "Response raw: " + httpResponse.raw());
                userData.logout();
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error obtaining token " + e.getMessage(), e);
            return null;
        }
    }

}
