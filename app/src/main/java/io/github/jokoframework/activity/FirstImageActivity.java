package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.simplerel.R;

/**
 * Created by joaquin on 23/08/17.
 */

public class FirstImageActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setImageResource(R.drawable.paraguay);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Se obtiene el id del item del action bar seleccionado
         * y se realiza la acción de acuerdo a éste
         */
        if (item.getItemId() == android.R.id.home) {
            backToHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
