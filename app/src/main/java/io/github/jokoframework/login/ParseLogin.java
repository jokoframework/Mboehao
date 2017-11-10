package io.github.jokoframework.login;


import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Map;

import io.github.jokoframework.activity.HomeActivity;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.logger.RemoteLogger;
import io.github.jokoframework.mboehaolib.util.ParseUtils;
import io.github.jokoframework.mboehaolib.util.SecurityUtils;
import io.github.jokoframework.mboehaolib.util.Utils;


public class ParseLogin implements Authenticable {

    private String user,password;
    private EditText userTextField,passTextField;

    private Map<String,String> custom;

    private String LOG_TAG =  ParseLogin.class.getSimpleName();
    private Button loginButton;
    private View progressBar;
    private CheckBox saveCredentials;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public ParseLogin(Button loginButton, View progressBar, Activity activity, CheckBox saveCredentials) {
        this.loginButton = loginButton;
        this.progressBar = progressBar;
        this.activity = activity;
        this.saveCredentials = saveCredentials;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public View getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Map<String, String> getCustom() {
        return custom;
    }

    @Override
    public boolean authenticate() {
        ParseUser.logInInBackground(getUser(), getPassword(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put(Constants.PARSE_ATTRIBUTE_USER_AS_USERNAME, user);
                    installation.setACL(ParseUtils.getDefaultAcl(ParseUtils.getCurrentUser()));

                    installation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(LOG_TAG, "Error guardando preferencias de aplicación", e);
                            }
                        }
                    });
                    loginSuccessful();
                } else if (user == null) {
                    usernameOrPasswordIsInvalid(e);
                } else {
                    somethingWentWrong();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getProgressBar().setVisibility(View.INVISIBLE);
                        getLoginButton().setEnabled(true);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public void setUserField(EditText userField) {
        this.userTextField = userField;
    }

    @Override
    public void setPassField(EditText passField) {
        this.passTextField = passField;
    }

    @Override
    public void setPassword(String pPassword) {
        this.password = pPassword;
    }

    @Override
    public void setUser(String pUser) {
        this.user = pUser;
    }

    @Override
    public void setCustom(Map<String, String> pCustom) {
        this.custom = pCustom;
    }

    private void loginSuccessful(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });
        RemoteLogger.v(LOG_TAG,"Login Successfully Done!");
    }

    private void usernameOrPasswordIsInvalid(ParseException e){
        Toast toast;
        switch (e.getCode()){
            case ParseException.OBJECT_NOT_FOUND:
                toast = Toast.makeText(getActivity(),"Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG);
                break;
            case ParseException.CONNECTION_FAILED:
                toast = Toast.makeText(getActivity(), "Sin conexion a internet", Toast.LENGTH_LONG);
                break;
            default:
                toast = Toast.makeText(getActivity(), "No se pudo hacer login", Toast.LENGTH_LONG);
        }
        toast.show();

        RemoteLogger.e(LOG_TAG,e.getMessage());
    }

    private void somethingWentWrong(){
        RemoteLogger.e(LOG_TAG,"Error al conectarse al servidor. ParseUser es null");
    }

    public void saveCredentials(){
        if (saveCredentials.isChecked()) {
            String usernameEncrypted = SecurityUtils.encrypt(user);
            String passwordEncrypted = SecurityUtils.encrypt(password);

            Utils.addPrefs(activity, Constants.USER_PREFS_USER, usernameEncrypted);
            Utils.addPrefs(activity, Constants.USER_PREFS_PW, passwordEncrypted);
        }
    }

}
