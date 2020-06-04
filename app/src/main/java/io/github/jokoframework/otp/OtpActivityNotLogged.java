package io.github.jokoframework.otp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import androidx.annotation.RequiresApi;

import io.github.jokoframework.R;
import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.otp.add.ScanActivity;
import io.github.jokoframework.otp.token.TokenAdapter;


public class OtpActivityNotLogged extends Activity{
    private TokenAdapter mTokenAdapter;
    public static final String ACTION_IMAGE_SAVED = "io.github.jokoframework.otp.ACTION_IMAGE_SAVED";
    RefreshListBroadcastReceiver receiver = new RefreshListBroadcastReceiver();

    private class RefreshListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTokenAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        findViewById(R.id.scanImage).setOnClickListener(v -> tryOpenCamera());

        findViewById(R.id.back1).setOnClickListener(v -> {
            Intent intent = new Intent(OtpActivityNotLogged.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        });

        mTokenAdapter = new TokenAdapter(this);
        registerReceiver(receiver, new IntentFilter(ACTION_IMAGE_SAVED));
        ((GridView) findViewById(R.id.grid)).setAdapter(mTokenAdapter);

        // Don't permit screenshots since these might contain OTP codes.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        DataSetObserver mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mTokenAdapter.getCount() == 0)
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                else
                    findViewById(android.R.id.empty).setVisibility(View.GONE);
            }
        };
        mTokenAdapter.registerDataSetObserver(mDataSetObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTokenAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTokenAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void tryOpenCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            int PERMISSIONS_REQUEST_CAMERA = 1;
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }
        else {
            // permission is already granted
            openCamera();
        }
    }

    private void openCamera() {
        startActivity(new Intent(this, ScanActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
