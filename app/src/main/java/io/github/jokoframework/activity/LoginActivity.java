package io.github.jokoframework.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
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

import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import io.github.jokoframework.BuildConfig;
import io.github.jokoframework.R;
import io.github.jokoframework.eula.Eula;
import io.github.jokoframework.login.Authenticable;
import io.github.jokoframework.login.CredentialsTextView;
import io.github.jokoframework.login.ParseLogin;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.SecurityUtils;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.misc.ProcessError;
import io.github.jokoframework.model.LoginRequest;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.repository.LoginRepository;
import io.github.jokoframework.repository.RepoBuilder;
import io.github.jokoframework.utilities.AppUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements ProcessError {

    private String LOG_TAG = LoginActivity.class.getSimpleName();
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private Profile currentUser;
    private EditText userTextField, passTextField;
    private ImageView imageLogin;
    private CheckBox saveCredentials;
    private Activity thisActivity;
    private Button enterButton;
    private Activity mySelf;


    public Activity thisActivity() {
        return thisActivity;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setActivity(Activity activity) {
        this.thisActivity = activity;
    }

    public void saveCredentials(String user, String pass) {
        if (saveCredentials.isChecked()) {
            String usernameEncrypted = SecurityUtils.encrypt(user);
            String passwordEncrypted = SecurityUtils.encrypt(pass);

            AppUtils.addPrefs(thisActivity(), Constants.USER_PREFS_USER, usernameEncrypted);
            AppUtils.addPrefs(thisActivity(), Constants.USER_PREFS_PW, passwordEncrypted);
        }
    }

    private boolean isPasswordValid(String password) {
        return AppUtils.isValidPassword(password);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_login);
        HomeActivity.cancelAlarmServices(this);
        setActivity(this);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        mySelf = this;
        Eula.show(mySelf);
        final String decryptedUser = SecurityUtils.decrypt(AppUtils.getPrefs(this, Constants.USER_PREFS_USER));
        final String decryptedPassword = SecurityUtils.decrypt(AppUtils.getPrefs(this, Constants.USER_PREFS_PW));
        enterButton = (Button) findViewById(R.id.buttonEnter);
        imageLogin = findViewById(R.id.imageLogin);
        LoginButton loginButton = findViewById(R.id.login_button);
        userTextField = (EditText) findViewById(R.id.user);
        passTextField = (EditText) findViewById(R.id.pass);
        saveCredentials = (CheckBox) findViewById(R.id.checkBox);
        userTextField.setText(decryptedUser);
        passTextField.setText(decryptedPassword);
        //Si el usuario es diferente se tiene que vaciar el password...
        CredentialsTextView credentialsTextView = new CredentialsTextView(userTextField, passTextField);
        credentialsTextView.userTextListener();
        enterButton.setOnClickListener(new ClickEnterHandler());
        loginButton.registerCallback(callbackManager, new FacebookCallbackLogin());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "A mostrar process desde fb:onClick");
                if (accessToken == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(true, "Login con Facebook...");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress(false);
                        }
                    });
                }
            }
        });
    }

    private class FacebookCallbackLogin implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            ProfileTracker mProfileTracker;
            //Mostramos el progress, hasta que vaya a la pantalla de transacciones
            Log.d(LOG_TAG, "A mostrar process desde fb:onSuccess");
            showProgress(true, "Login con Facebook...");
            Utils.showToast(getBaseContext(),
                    "Login Facebook Success");
            final AccessToken fbAccessToken = loginResult.getAccessToken();
            Log.i(LOG_TAG, String.format("Access token %s, UserId: %s",
                    fbAccessToken.getToken(),
                    fbAccessToken.getUserId()));
            LoginRequest loginRequest = LoginRequest.builder(fbAccessToken);
            loginRequest.getCustom().put("deviceIdentifier", getGcmDeviceIdentifier());
            userLogin(loginRequest);
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {
                    currentUser = currentProfile;

                    if (currentUser != null) {
                        Utils.showToast(getBaseContext(),
                                String.format("Hola %s.", currentUser.getFirstName()));
                    }
                    Log.d(LOG_TAG, "A ocultar process desde fb:onCurrentProfileChanged");
                    showProgress(false);
                }
            };
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {
                    // Set the access token using
                    // currentAccessToken when it's loaded or set.
                    LoginActivity.this.accessToken = currentAccessToken;
                    if (currentAccessToken == null) {
                        Log.d(LOG_TAG, "A ocultar process desde fb:onCurrentAccessTokenChanged");
                        showProgress(false);
                    }
                }
            };
            accessTokenTracker.startTracking();
        }

        @Override
        public void onCancel() {
            Utils.showToast(getBaseContext(),
                    "Login Facebook cancelado");
            Log.d(LOG_TAG, "A ocultar process desde fb:onCancel");
            showProgress(false);
        }

        @Override
        public void onError(FacebookException error) {
            Utils.showToast(getBaseContext(),
                    String.format("Login Facebook erróneo: %s", error));
            Log.d(LOG_TAG, "A ocultar process desde fb:onError");
            showProgress(false);
        }
    }


    private class ClickEnterHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String username = userTextField.getText().toString();
            final String password = passTextField.getText().toString();

            // HomeActivity progress
            showProgress(true, "Ingresando..."); // Muestra el progress bar mientras se obtine el acceso...

//            -----LOGIN WITH PARSE--------------------------
            if(Boolean.parseBoolean(getString(R.string.parse_enabled))) {
                Authenticable parseLogin = new ParseLogin(enterButton, mySelf, saveCredentials);
                parseLogin.setPassword(password);
                parseLogin.setUser(username);
                parseLogin.saveCredentials();
                parseLogin.authenticate();
            }

////            ------JWT LOGIN---------------------------------
            saveCredentials(username, password);
            attemptLogin();
        }
    }

    private void sendErrorLoginMessage(String message) {
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
            passTextField.setError(String.format("%s", R.string.error_field_required));
            focusView = passTextField;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passTextField.setError(String.format("%s", R.string.error_invalid_password));
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
            userLogin(new LoginRequest(username, password));
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

    private void userLogin(LoginRequest loginRequest) {
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
                    public void onNext(String deviceIdentifier) {
                        loginRequest.getCustom().put("deviceIdentifier", deviceIdentifier);
                        makeLoginRequest(loginRequest, authApi);
                    }
                });
    }


    private void makeLoginRequest(LoginRequest loginRequest, LoginRepository authApi) {
        loginRequest.getCustom().put("deviceType", Constants.DEVICE_TYPE);
        loginRequest.getCustom().put("deviceName", Build.MODEL);
        loginJWT(loginRequest);
        //doLogin(loginRequest, authApi);
    }


    private void doLogin(LoginRequest loginRequest, LoginRepository authApi) {

        UserData userData = getUserData();
        authApi.login(loginRequest, Constants.CURRENT_MBOEHAO_VERSION)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(AppUtils.errorHandler(LoginActivity.this))
                .flatMap(userAccessResponse -> {
                    try {
                        userData.setExpiration(userAccessResponse.getExpiration());
                        getUserData().setRefreshToken(userAccessResponse.getSecret());
                        getUserData().setExpiration(userAccessResponse.getExpiration());
                        getUserData().setUsername(loginRequest.getUsername());
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
                        showProgress(true, "Login exitoso.");
                        // Se accede al home... todavia los token no son necesarios...
                        // ...para acceder a ningun servicio, ya que no hay todavia ninguno disponible...
                        //loginJWT(loginRequest);
                    } else {
                        Log.i(LOG_TAG, "login: Success false");
                        sendErrorLoginMessage("Login: False Success");
                        invalidLogin();
                    }
                }, AppUtils.errorHandler(LoginActivity.this));
    }

    private void invalidLogin() {
        logout();
        userTextField.setError(String.format("%s", R.string.error_incorrect_credentials));
        passTextField.setError(String.format("%s", R.string.error_incorrect_credentials));
        userTextField.requestFocus();
    }

    private void logout() {
        UserData userData = getUserData();
        userData.logout();
    }

    private void loginJWT(LoginRequest loginRequest) {
        final TextView mTextView = (TextView) findViewById(R.id.text);

        Intent i = new Intent(thisActivity, HomeActivity.class);
        //thisActivity().startActivity(i);// Instantiate the RequestQueue.

        // Instanciar el RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.jwt_URL);

        Map<String, String> params = new HashMap();
        params.put("username",loginRequest.getUsername());
        params.put("password",loginRequest.getPassword());

        JSONObject parameters = new JSONObject(params);

        // Solicitar un JSON como respuesta de la URL.
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    // Mostrar el response (DEBUG)
                    //Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

                    // Verificar login exitoso
                    String loginSuccess;
                    try {
                        loginSuccess = response.getString("success");
                        if (loginSuccess.equals("true")){
                            Utils.showToast(getBaseContext(), String.format("Login succesful."));
                            thisActivity().startActivity(i);// Iniciar Home activity
                            //loginSuccessful();
                        } else {
                            Utils.showToast(getBaseContext(), String.format("Login failed."));
                            showProgress(false);
                            invalidLogin();
                        }
                    } catch (Exception e){
                        Log.e(LOG_TAG, "Error at JWT Login " + e.getMessage(), e);
                    };
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Utils.showToast(getBaseContext(), String.format("ERROR: "+ error.toString()));
                    showProgress(false);
                    invalidLogin();
                }
            }
        );


        // Add the request to the RequestQueue.
        postRequest.setShouldCache(false);
        queue.add(postRequest);

        //thisActivity().finish();
        showProgress(false);
    }

    private void loginSuccessful() {
        Intent i = new Intent(thisActivity, HomeActivity.class);
        thisActivity.startActivity(i);
        thisActivity.finish();
    }

    @Override
    public void afterProcessErrorNoConnection() {
        showNoConnectionAndQuit();
    }

    @Override
    public void afterProcessError(UserAccessResponse response) {
        AppUtils.showError(response, imageLogin, null);
    }

    @Override
    public void afterProcessError(String message) {
        AppUtils.showError(message, imageLogin, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final UserData userData = getUserData();
        if (userData != null && userData.isValid()) {
            showProgress(true, "Recuperando información de usuario...");
            loginSuccessful();
        } else if (userData == null) {
            invalidLogin();
        } else {
            logout();
        }
        Log.d(LOG_TAG, "A ocultar process desde onResume");
        showProgress(false);
    }
}

