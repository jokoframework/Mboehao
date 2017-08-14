package io.github.jokoframework.singleton;

/**
 * Created by joaquin on 08/08/17.
 */
import com.parse.Parse;
import android.app.Application;
//This class just initialize the Parse Server with the URL and appID... configuration in the Android Manifest
public class MboehaoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);
    }
}
