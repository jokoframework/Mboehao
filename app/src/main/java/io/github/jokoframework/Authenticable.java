package io.github.jokoframework;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Map;

/**
 * Created by joaquin on 09/08/17.
 */

public interface Authenticable {

    public String getPassword();
    public String getUser();
    public Map<String,String> getCustom();
    public boolean authenticate();

    public void setPassword(String pPassword);
    public void setUser(String pUser);
    public void setCustom(Map<String, String> pCustom);
    public Button getLoginButton();

    public void setLoginButton(Button loginButton) ;

    public View getProgressBar() ;

    public void setProgressBar(ProgressBar progressBar);

    public void saveCredentials();

}
