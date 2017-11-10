package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.parse.ParseUser;

/**
 * Created by joaquin on 09/11/17.
 */

public class LogOutActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent lgin = new Intent(this, LoginActivity.class);
        facebookLogout();
        doLogout(lgin);
    }

    private void facebookLogout() {
        LoginManager.getInstance().logOut();
    }

    private void doLogout(Intent intent) {
        ParseUser.logOut();
        startActivity(intent);
        finish();
    }
}
