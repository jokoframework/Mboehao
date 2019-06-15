package io.github.jokoframework.otp.edit;

import android.app.Activity;
import android.os.Bundle;

import io.github.jokoframework.BuildConfig;


public abstract class BaseActivity extends Activity {
    public static final String EXTRA_POSITION = "position";
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the position of the token. This MUST exist.
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, -1);
        if(BuildConfig.DEBUG && mPosition < 0)
            throw new RuntimeException("Could not create BaseActivity");
    }

    protected int getPosition() {
        return mPosition;
    }
}
