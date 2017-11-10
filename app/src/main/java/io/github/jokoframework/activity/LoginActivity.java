package io.github.jokoframework.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.jokoframework.login.Authenticable;
import io.github.jokoframework.login.ParseLogin;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.logger.RemoteLogger;
import io.github.jokoframework.mboehaolib.util.SecurityUtils;
import io.github.jokoframework.mboehaolib.util.Utils;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import io.github.jokoframework.eula.Eula;
import io.github.jokoframework.misc.ProcessError;
import io.github.jokoframework.misc.Utilitys;
import io.github.jokoframework.login.CredentialsTextView;
import io.github.jokoframework.model.LoginRequest;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.repository.LoginRepository;
import io.github.jokoframework.repository.RepoBuilder;
import io.github.jokoframework.singleton.MboehaoApp;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import io.github.jokoframework.R;
import rx.schedulers.Schedulers;


import java.io.IOException;
import java.util.Arrays;


public class LoginActivity extends Activity implements ProcessError {

    private static MboehaoApp application;

    public final synchronized MboehaoApp getApp() {
        if (application == null) {
            application = (MboehaoApp) getApplicationContext();
        }
        return application;
    }

    public final UserData getUserData() {
        return getApp().getUserData();
    }

    private ProfileTracker mProfileTracker;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private Profile currentUser;


    private EditText userTextField,passTextField;

    private String LOG_TAG =  LoginActivity.class.getSimpleName();
    private View progressView;
    private ImageView imageLogin;
    private CheckBox saveCredentials;
    private Activity thisActivity;


    public Activity thisActivity() {
        return thisActivity;
    }

    CallbackManager callbackManager = CallbackManager.Factory.create();

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setActivity(Activity activity) {
        this.thisActivity = activity;
    }

    public void saveCredentials(String user, String pass){
        if (saveCredentials.isChecked()) {
            String usernameEncrypted = SecurityUtils.encrypt(user);
            String passwordEncrypted = SecurityUtils.encrypt(pass);

            Utils.addPrefs(thisActivity(), Constants.USER_PREFS_USER, usernameEncrypted);
            Utils.addPrefs(thisActivity(), Constants.USER_PREFS_PW, passwordEncrypted);
        }
    }

    private boolean isPasswordValid(String password) {
        return Utils.isValidPassword(password);
    }

    private Button enterButton;
    private Activity mySelf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HomeActivity.cancelAlarmServices(this);
        setActivity(this);
        Fabric.with(this, new Crashlytics());
        mySelf = this;
        Eula.show(mySelf);
        final String decryptedUser = SecurityUtils.decrypt(Utils.getPrefs(this, Constants.USER_PREFS_USER));
        final String decryptedPassword = SecurityUtils.decrypt(Utils.getPrefs(this, Constants.USER_PREFS_PW));
        facebookLogin();
        enterButton = (Button) findViewById(R.id.buttonEnter);
        userTextField = (EditText) findViewById(R.id.user);
        passTextField = (EditText) findViewById(R.id.pass);
        saveCredentials = (CheckBox) findViewById(R.id.checkBox);
        userTextField.setText(decryptedUser);
        passTextField.setText(decryptedPassword);
        //Si el usuario es diferente se tiene que vaciar el password...
        CredentialsTextView credentialsTextView = new CredentialsTextView(userTextField,passTextField);
        credentialsTextView.userTextListener();
        enterButton.setOnClickListener(new clickEnter());
    }

    public void facebookLogin(){
        Profile user = Profile.getCurrentProfile();
        if(user != null){
            loginSuccessful();
        } else{
            LoginButton loginButton = findViewById(R.id.login_button);
            loginButton.setReadPermissions("public_profile");
            // Callback registration
            if(Utils.isNetworkAvailable(mySelf)) {
                currentUser = Profile.getCurrentProfile();
                accessToken = AccessToken.getCurrentAccessToken();

                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mProfileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(
                                    Profile oldProfile,
                                    Profile currentProfile) {
                                currentUser = currentProfile;
                                // App code
                            }
                        };

                        accessTokenTracker = new AccessTokenTracker() {
                            @Override
                            protected void onCurrentAccessTokenChanged(
                                    AccessToken oldAccessToken,
                                    AccessToken currentAccessToken) {
                                // Set the access token using
                                // currentAccessToken when it's loaded or set.
                                accessToken = currentAccessToken;
                            }
                        };
                        if(currentUser != null)
                            Utils.addPrefs(thisActivity(),Constants.FACEBOOK_PROFILE_DATA,currentUser.getName());
                        loginSuccessful();
                    }
                    @Override
                    public void onCancel() {
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }else{
                Toast.makeText(thisActivity,"No tiene conexion a internet!!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class clickEnter implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            final String username = userTextField.getText().toString();
            final String password = passTextField.getText().toString();

            progressView = findViewById(R.id.progressMainWindow); // progress bar...
            imageLogin = findViewById(R.id.imageLogin);
            // HomeActivity progress
            progressView.setVisibility(View.VISIBLE); // Muestra el progress bar mientras se obtine el acceso...

//            -----LOGIN WITH PARSE--------------------------
            Authenticable parseLogin = new ParseLogin(enterButton,progressView, mySelf,saveCredentials);
            parseLogin.setPassword(password);
            parseLogin.setUser(username);
            parseLogin.saveCredentials();
            parseLogin.authenticate();

////            ------JWT LOGIN---------------------------------
//            saveCredentials(username,password);
//            attemptLogin();
        }
    }

    private void sendErrorLoginMessage(String message){
        Utils.showStickyMessage(this, message);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid number, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        // Reset errors.
        imageLogin.setVisibility(View.GONE);
        userTextField.setError(null);
        passTextField.setError(null);

        // Store values at the time of the login attempt.
        String username = userTextField.getText().toString();
        String password = passTextField.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passTextField.setError(String.format("%s",R.string.error_field_required));
            focusView = passTextField;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passTextField.setError(String.format("%s",R.string.error_invalid_password));
            focusView = passTextField;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            userLogin(username,password);
        }
    }

    private String getGcmDeviceIdentifier() {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String deviceIdentifier = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(LOG_TAG, "GCM Device Identifier: " + deviceIdentifier);
            return deviceIdentifier;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting GCM instanceID " + e.getMessage(), e);
            return null;
        }
    }

    private void userLogin(String username, String password) {
        LoginRepository authApi = RepoBuilder.getInstance(LoginRepository.class);
        Observable.defer(() -> {
            String token = getGcmDeviceIdentifier();
            return Observable.just(token);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        //no se usa
                    }
                    @Override
                    public void onError(Throwable e) {
                        sendErrorLoginMessage(e.getMessage());
                    }
                    @Override
                    public void onNext(String token) {
                        makeLoginRequest(username, password, authApi);
                    }
                });
    }

    private void makeLoginRequest(String username, String password, LoginRepository authApi) {
        LoginRequest loginRequest;
        loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        loginProcess(loginRequest, authApi, username);
    }

    private void loginProcess(LoginRequest loginRequest, LoginRepository authApi, String username) {
        long startTime = System.currentTimeMillis();
        long endTime = 15 * 1000; // after 15 second, it ends and return and Error!...

        UserData userData = getUserData();
        authApi.login(loginRequest, Constants.CURRENT_MBOEHAO_VERSION)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Utilitys.errorHandler(LoginActivity.this))
                .flatMap(userAccessResponse -> {
                    try {
                        userData.setExpiration(userAccessResponse.getExpiration());
                        getUserData().setRefreshToken(userAccessResponse.getSecret());
                        getUserData().setExpiration(userAccessResponse.getExpiration());
                        getUserData().setUsername(username);
                        getUserData().persistUser();
                        return authApi.userAccess(userAccessResponse.getSecret());
                    } catch (Exception t) {
                        return Observable.error(t);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userAccessResponse -> {
                    if (userAccessResponse.getSuccess()) {
                        userData.setUserAccessToken(userAccessResponse.getSecret());
                        // Se accede al home... todavia los token no son necesarios...
                        // ...para acceder a ningun servicio, ya que no hay todavia ninguno disponible...
                        loginJWT();
                    } else {
                        Log.i(LOG_TAG, "login: Success false");
                        sendErrorLoginMessage("Login: False Success");
                        invalidLogin();
                    }
                }, Utilitys.errorHandler(LoginActivity.this));
        progressView.setVisibility(View.INVISIBLE); // Muestra el progress bar mientras se obtine el acceso...
    }

    private void invalidLogin() {
        UserData userData = getUserData();
        userData.logout();
        userTextField.setError(String.format("%s", R.string.error_incorrect_credentials));
        passTextField.setError(String.format("%s", R.string.error_incorrect_credentials));
        userTextField.requestFocus();
    }

    private void loginJWT(){
        Intent i = new Intent(thisActivity, HomeActivity.class);
        thisActivity().startActivity(i);
        thisActivity().finish();
        progressView.setVisibility(View.INVISIBLE);
    }

    private void loginSuccessful(){
        Intent i = new Intent(thisActivity, HomeActivity.class);
        thisActivity.startActivity(i);
        thisActivity.finish();
    }

    @Override
    public void afterProcessErrorNoConnection() {
        startActivity(Utilitys.createIntentNoConnection(this));
    }

    @Override
    public void afterProcessError(UserAccessResponse response) {
        Utilitys.showError(response, imageLogin, null);
    }

    @Override
    public void afterProcessError(String message) {
        Utilitys.showError(message, imageLogin, null);
    }

}

