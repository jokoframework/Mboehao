package io.github.jokoframework.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.jokoframework.R;
import io.github.jokoframework.constants.AppConstants;
import io.github.jokoframework.eula.Eula;
import io.github.jokoframework.login.CredentialsTextView;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.SecurityUtils;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.misc.ProcessError;
import io.github.jokoframework.model.LoginRequest;
import io.github.jokoframework.model.UserAccessResponse;
import io.github.jokoframework.model.UserData;
import io.github.jokoframework.otp.OtpActivityNotLogged;
import io.github.jokoframework.singleton.MboehaoApp;
import io.github.jokoframework.utilities.AppUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements ProcessError {

    private String LOG_TAG = LoginActivity.class.getSimpleName();
    private EditText userTextField, passTextField;
    private ImageView imageLogin, imageView;
    private CheckBox saveCredentials;
    private Activity thisActivity;
    private Button enterButton;
    private Activity mySelf;
    private Button otpButton;
    private String token = null;

    public Activity thisActivity() {
        return thisActivity;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializeUI();
        MboehaoApp.prompDevHostName(this);
    }

    private void initializeUI() {
        setContentView(R.layout.activity_login);
        // HomeActivity.cancelAlarmServices(this);
        HomeActivity.cancelAlarmServices(this);

        try {
            Intent mServiceIntent = new Intent(getBaseContext(), io.github.jokoframework.service.CountryHelper.class);
            getBaseContext().startService(mServiceIntent);
        } catch (RuntimeException e) {
            Utils.showToast(getBaseContext(), "Fallo de CountryHelper");
            Log.e(LOG_TAG, getBaseContext().getString(R.string.no_network_connection), e);
        }
        setActivity(this);
        mySelf = this;
        Eula.show(mySelf);
        final String decryptedUser = SecurityUtils.decrypt(AppUtils.getPrefs(this, Constants.USER_PREFS_USER));
        final String decryptedPassword = SecurityUtils.decrypt(AppUtils.getPrefs(this, Constants.USER_PREFS_PW));
        imageLogin = findViewById(R.id.imageLogin);
        imageView = findViewById(R.id.imageView);
        userTextField = findViewById(R.id.user);
        passTextField = findViewById(R.id.pass);
        saveCredentials = findViewById(R.id.checkBox);
        enterButton = findViewById(R.id.buttonEnter);
        otpButton = findViewById(R.id.buttonOtp);
        userTextField.setText(decryptedUser);
        passTextField.setText(decryptedPassword);

        //Si el usuario es diferente se tiene que vaciar el password...
        CredentialsTextView credentialsTextView = new CredentialsTextView(userTextField, passTextField);
        credentialsTextView.userTextListener();
        enterButton.setOnClickListener(new ClickEnterHandler());
        otpButton.setOnClickListener(new ClickOtpHandler());
    }

    private class ClickEnterHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final String username = userTextField.getText().toString();
            final String password = passTextField.getText().toString();

            // HomeActivity progress
            showProgress(true, "Ingresando..."); // Muestra el progress bar mientras se obtine el acceso...


////            ------JWT LOGIN---------------------------------
            saveCredentials(username, password);
            attemptLogin();
        }
    }
    private class ClickOtpHandler implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent i = new Intent(thisActivity, OtpActivityNotLogged.class);
            thisActivity().startActivity(i);
            finish();
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

    private void getFcmDeviceIdentifier() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                token = Objects.requireNonNull(task.getResult()).getToken();
                Log.i(LOG_TAG, "GCM Device Identifier: " + token);
            } else {
                Log.i(LOG_TAG, Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                token = null;
            }
        });
    }

    private void userLogin(LoginRequest loginRequest) {
        //LoginRepository authApi = RepoBuilder.getInstance(LoginRepository.class);
        Observable.defer(() -> {
            getFcmDeviceIdentifier();
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
                        makeLoginRequest(loginRequest);
                    }
                });
    }

    private void makeLoginRequest(LoginRequest loginRequest) {
        loginRequest.getCustom().put("deviceType", Constants.DEVICE_TYPE);
        loginRequest.getCustom().put("deviceName", Build.MODEL);
        loginJWT(loginRequest);
        //doLogin(loginRequest, authApi);
    }

    /*
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
    */

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

        Intent i = new Intent(thisActivity, SecondAuthenticationActivity.class);

        // Instanciar el RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MboehaoApp.getHostName() + getString(R.string.jwt_URL);

        Map<String, String> params = new HashMap();
        params.put("username", loginRequest.getUsername());
        params.put("password", loginRequest.getPassword());

        JSONObject parameters = new JSONObject(params);

        // Solicitar un JSON como respuesta de la URL.
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {
            // Mostrar el response (DEBUG)
            //Utils.showToast(getBaseContext(), String.format("Response is: "+ response.toString()));

            // Verificar login exitoso
            String loginSuccess;
            String secret;
            try {
                loginSuccess = response.getString("success");
                if (loginSuccess.equals("true")){
                    secret = response.getString("secret");
                    i.putExtra(AppConstants.SECRET, secret);
                    Utils.showToast(getBaseContext(), "Login succesful.");
                    thisActivity().startActivity(i);// Iniciar Home activity
                    //loginSuccessful();
                } else {
                    Utils.showToast(getBaseContext(), "Login failed.");
                    showProgress(false);
                    invalidLogin();
                }
            } catch (Exception e){
                Log.e(LOG_TAG, "Error at JWT Login " + e.getMessage(), e);
            }
        }, error -> {
            Utils.showToast(getBaseContext(), "ERROR: "+ error.toString());
            showProgress(false);
            invalidLogin();
        }
        );

        // Add the request to the RequestQueue.
        postRequest.setShouldCache(false);
        queue.add(postRequest);

        //thisActivity().finish();
        showProgress(false);
    }

    private void loginSuccessful() {
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
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

