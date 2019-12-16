package io.github.jokoframework.otp;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import io.github.jokoframework.R;
import io.github.jokoframework.otp.add.ScanActivity;
import io.github.jokoframework.otp.token.TokenAdapter;


public class OtpActivity extends Activity{
    private TokenAdapter mTokenAdapter;
    public static final String ACTION_IMAGE_SAVED = "io.github.jokoframework.otp.ACTION_IMAGE_SAVED";
    private DataSetObserver mDataSetObserver;
    private final int PERMISSIONS_REQUEST_CAMERA = 1;
    private RefreshListBroadcastReceiver receiver;

    private class RefreshListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTokenAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mTokenAdapter = new TokenAdapter(this);
        receiver = new RefreshListBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(ACTION_IMAGE_SAVED));
        ((GridView) findViewById(R.id.grid)).setAdapter(mTokenAdapter);

        // Don't permit screenshots since these might contain OTP codes.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        mDataSetObserver = new DataSetObserver() {
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.otp_main, menu);
        menu.findItem(R.id.action_scan).setVisible(ScanActivity.hasCamera(this));
        menu.findItem(R.id.action_scan).setOnMenuItemClickListener(this::onOptionsItemSelected);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void tryOpenCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Se obtiene el id del item del action bar seleccionado
         * y se realiza la acción de acuerdo a éste
         */
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.action_scan){
            tryOpenCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
