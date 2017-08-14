package io.github.jokoframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.example.simplerel.R;

/**
 * Created by joaquin on 23/08/17.
 */

public class MenuHeaderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.side_menu_header);
    }

}
