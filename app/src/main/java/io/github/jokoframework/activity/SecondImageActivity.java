package io.github.jokoframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.example.simplerel.R;

/**
 * Created by joaquin on 25/08/17.
 */

public class SecondImageActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView imagen = (ImageView) findViewById(R.id.imageView1);
        imagen.setImageResource(R.drawable.joko_round);
    }

}
