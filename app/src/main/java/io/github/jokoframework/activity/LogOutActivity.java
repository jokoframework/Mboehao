package io.github.jokoframework.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Created by joaquin on 09/11/17.
 */

public class LogOutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent login = new Intent(this, LoginActivity.class);
        doLogout(login);
    }

    private void doLogout(Intent intent) {
        if(getApp().getUserData() != null) {
            getApp().getUserData().logout();
        }
        startActivity(intent);
        finish();
    }
}
