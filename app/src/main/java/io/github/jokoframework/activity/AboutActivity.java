package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;

import io.github.jokoframework.R;

public class AboutActivity extends Activity implements ImageView.OnClickListener {

    private final int NUM_CLICKS_REQUIRED = 6;

    //List treated circularly to track last NUM_CLICKS_REQUIRED number of clicks
    private long[] clickTimestamps = new long[NUM_CLICKS_REQUIRED];
    private int oldestIndex = 0;
    private int nextIndex = 0;
    //-------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initilizeUI();
    }

    private void initilizeUI() {
        setContentView(R.layout.activity_about);

        findViewById(R.id.volver).setOnClickListener(v -> {
            Intent intent = new Intent(AboutActivity.this, Home2Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        });

        TextView tv = findViewById(R.id.version_number);
        tv.setText(getString( R.string.version_name));
        ImageView jokoAbout = findViewById(R.id.imageViewHelp);
        jokoAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        long timeMillis = (new Date()).getTime();
        // If we have at least the min number of clicks on record
        if (nextIndex == (NUM_CLICKS_REQUIRED - 1) || oldestIndex > 0) {
            // Check that all required clicks were in required time
            int diff = (int) (timeMillis - clickTimestamps[oldestIndex]);
            //------- Instrumentation to handle number of clicks on default medication switch --------//
            //Require X clicks in Y seconds to trigger secret action
            double SECONDS_FOR_CLICKS = 2;
            if (diff < SECONDS_FOR_CLICKS * 1000) {
                // if accomplish then...
                showAlertDialog(this,"Easter-Egg!!",getString(R.string.easter_egg_msg));
            }
            oldestIndex++;
        }
        // If not done, record click time, and bump indices
        clickTimestamps[nextIndex] = timeMillis;
        nextIndex++;
        if (nextIndex == NUM_CLICKS_REQUIRED) {
            nextIndex = 0;
        }
        if (oldestIndex == NUM_CLICKS_REQUIRED) {
            oldestIndex = 0;
        }
    }

    private void showAlertDialog(Context mContext, String mTitle, String mBody){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        builder.setCancelable(true);

        if(mTitle.length()>0)
            builder.setTitle(mTitle);
        if(mBody.length()>0)
            builder.setTitle(mBody);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

}
