package io.github.jokoframework.auth;

import android.content.Context;

import java.io.IOException;

import io.github.jokoframework.R;
import io.github.jokoframework.activity.BaseActivity;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.singleton.MboehaoApp;
import io.github.jokoframework.utilities.AppUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private MboehaoApp application;

    public AuthInterceptor(MboehaoApp application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        //final String server = application.getString(R.string.server_hostname);
        if (!AppUtils.isDnsWorking(Constants.SERVER_NAME)) {
            //Workaround - afeltes
            //Sin conexion, explotaba el APP por un UnknownHostException
            final Context applicationContext = application.getApplicationContext();
            AppUtils.showNoConnectionError(applicationContext);
            Request dummyRequest = new Request.Builder()
                    .url(applicationContext.getString(R.string.dummyHostname))
                    .build();
            return chain.proceed(dummyRequest);
        } else {
            BaseActivity activity = application.getBaseActivity();
            if (activity != null) {
                activity.setWithInternetConnection(true);
            }
            Request request = chain.request();
            //Build new request
            Request.Builder builder = request.newBuilder();
            String userAccessToken = null;
            UserData userData = application.getUserData();
            if (userData != null) {
                userAccessToken = userData.getUserAccessToken();
            }
            setAuthHeader(builder, userAccessToken); //write current token to request
            request = builder.build(); //overwrite old request
            return chain.proceed(request);
        }
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) { //Add Auth token to each request if authorized
            builder.header(Constants.HEADER_AUTH, token);
        }
    }

}
