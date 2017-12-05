package io.github.jokoframework.login;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Map;

/**
 * Created by joaquin on 09/08/17.
 *
 * @author joaquin
 * @author afeltes
 */

public interface Authenticable {

    String getPassword();

    String getUser();

    Map<String, String> getCustom();

    boolean authenticate();

    void setPassword(String pPassword);

    void setUser(String pUser);

    void setCustom(Map<String, String> pCustom);

    Button getLoginButton();

    void setLoginButton(Button loginButton);

    View getProgressBar();

    void setProgressBar(ProgressBar progressBar);

    void saveCredentials();

}
