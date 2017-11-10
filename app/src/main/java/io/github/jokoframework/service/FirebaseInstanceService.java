package io.github.jokoframework.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import io.github.jokoframework.mboehaolib.constants.Constants;
/**
 * Created by joaquin on 10/10/17.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService {
    public static final String LOG_TAG = FirebaseInstanceService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //TODO:should send the token to server...
        //DONE...
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(String.format("%s@gcm.googleapis.com",Constants.SENDER_ID))
                .setMessageId(String.format("%d",(Constants.msgId + 1)))
                .addData("token", refreshedToken)
                .build());
    }
}
