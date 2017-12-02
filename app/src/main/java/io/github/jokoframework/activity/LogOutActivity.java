package io.github.jokoframework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;

/**
 * Created by joaquin on 09/11/17.
 */

public class LogOutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent login = new Intent(this, LoginActivity.class);
        facebookLogout();
        doLogout(login);
    }

    private void facebookLogout() {
        LoginManager.getInstance().logOut();
    }

    private void doLogout(Intent intent) {
        ParseUser.logOut();
        if(getApp().getUserData() != null) {
            getApp().getUserData().logout();
        }
        startActivity(intent);
        finish();
    }
}
