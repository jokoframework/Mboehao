package io.github.jokoframework.login;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Map;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.logger.RemoteLogger;
import io.github.jokoframework.utilitys.SecurityUtils;
import io.github.jokoframework.activity.ShowActivity;
import io.github.jokoframework.utilitys.Utils;


public class ParseLogin implements Authenticable {

    private String user,password;

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
                Intent i = new Intent(getActivity(), ShowActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });
        RemoteLogger.e(LOG_TAG,"");
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
        /* TODO: cambio por la clase REMOTELOGGER...
        * */

    }

    private void somethingWentWrong(){

        RemoteLogger.e(LOG_TAG,"Error al conectarse al servidor. ParseUser es null");
        /* TODO: cambio por la clase REMOTELOGGER...
        * */
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
