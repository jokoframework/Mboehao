package io.github.jokoframework.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.jokoframework.R;

public class PreLoginActivity extends AppCompatActivity {

    Animation topAnim, botAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Animaciones
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        setContentView(R.layout.activity_pre_login);

        TextView textView = findViewById(R.id.textView10);
        ImageView mLogo = findViewById(R.id.mLogo);

        textView.setAnimation(botAnim);
        mLogo.setAnimation(topAnim);

        int SPLASH_SCREEN = 5000;
        new Handler() .postDelayed(() -> {
            Intent intent = new Intent(PreLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}
