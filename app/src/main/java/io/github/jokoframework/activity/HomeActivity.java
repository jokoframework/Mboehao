package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.otp.OtpActivity;
import io.github.jokoframework.service.TestServiceNotification;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onResume() {
        super.onResume();
        showAppNews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home2);

        WebView webView = this.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient(this));
        displayWebView(webView); //display de webUrl after set some settings of the WebView...
        startAlarmServices(this);
        startRESTServices(this);
        sendToken();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.mainNavView);

        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.menuImage).setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        findViewById(R.id.shareMain).setOnClickListener(v -> {
            Intent iShare = new Intent(HomeActivity.this, ShareActivity.class);
            startActivity(iShare);
            finish();
        });

        findViewById(R.id.optionsMain).setOnClickListener(v -> {
            Intent iPref = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(iPref);
            finish();
        });

        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(HomeActivity.this);

        dialogBuilder.setTitle("LOG OUT?").setMessage("You will return to Login Screen if you leave.")
        .setPositiveButton("YES", (dialog, which) -> {
            Intent leave = new Intent(HomeActivity.this, LogOutActivity.class);
            startActivity(leave);
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        })
        .setNegativeButton("NO", (dialog, which) -> {

        });

        dialogBuilder.show();
    }

    private void sendToken() {
        // Get token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();

            // Log,Send and toast the Token...
            String msg = String.format("%s - %s", R.string.msg_token_fmt, token);
            sendMessage(token);
            Log.d(LOG_TAG, msg);
        });
    }

    public void sendMessage(String msg) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(String.format("%s@gcm.googleapis.com", Constants.SENDER_ID))
                .setMessageId(String.format("%d", (Constants.msgId + 1)))
                .addData("token", msg)
                .build());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent option;

        switch (menuItem.getItemId()) {
            case R.id.lineChart:
                option = new Intent(HomeActivity.this, LineChartActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.barChart:
                option = new Intent(HomeActivity.this, BarChartActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.horiBarChart:
                option = new Intent(HomeActivity.this, HorizontalBarChartActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.multipleLineChart:
                option = new Intent(HomeActivity.this, MultipleLineChartActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.oneTimePass:
                option = new Intent(HomeActivity.this, OtpActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.roomPersistanceC:
                option = new Intent(HomeActivity.this, CountryActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.changePass:
                option = new Intent(HomeActivity.this, OptionsActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.about:
                option = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(option);
                finish();
                break;

            case R.id.logOut:
                onBackPressed();
                break;
        }

        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        private final View progressBarView = findViewById(R.id.progressHomeWindow);         // progress bar Parent...
        private WebView webView = findViewById(R.id.webview);
        private Activity activity;

        private MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(LOG_TAG, String.format("Loading %s", url));
            progressBarView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.INVISIBLE);
            progressBarView.setActivated(true);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(String.valueOf(request.getUrl()));
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        public void onPageFinished(WebView view, String url) {
            showFinalResults();
        }

        private void showFinalResults() {
            try {
                if (progressBarView != null && progressBarView.isActivated()) {
                    progressBarView.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                }
            } catch (Exception exception) {
                Log.e(LOG_TAG, getString(R.string.errorLoadingPage), exception);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

    }

    /*private void doLogout(Intent intent) {
        cancelAlarmServices(this);
        startActivity(intent);
        finish();
    }*/

    public static class MainViewFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_show_info, container, false);
        }
    }

    private void displayWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(getString(R.string.urlWiki));
    }

    public void startAlarmServices(Context context) {
        if (context != null) {
            TestServiceNotification.setAlarm(context);
        } else {
            Log.e(LOG_TAG, "Intentando inciar los servicios periodicos con un context null");
        }
    }

    public static void cancelAlarmServices(Context context) {
        if (context != null) {
            TestServiceNotification.cancelAlarm(context);
        } else {
            Log.e(LOG_TAG, "Intentando inciar los servicios periodicos con un context null");
        }
    }

    public void startRESTServices(Context context) {
        try {
            Intent mServiceIntent = new Intent(context, io.github.jokoframework.service.CronService.class);
            context.startService(mServiceIntent);
        } catch (RuntimeException e) {
            Utils.showToast(getBaseContext(), "Fallo de API REST");
            Toast.makeText(context, context.getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, context.getString(R.string.no_network_connection), e);
        }
    }

    protected void showAppNews() {
        long appNewsShowedTime = Utils.getLongPrefs(this, Constants.USER_PREFERENCE_APP_NEWS_SHOWED_TIME);
//        String appNews = AppUtils.getPrefs(this, AppConstants.USER_PREFERENCE_APP_NEWS);
        String appNews = getString(R.string.appnews);
        if (StringUtils.isNotBlank(appNews)
                && (appNewsShowedTime <= 0L || !showedInLastHour(appNewsShowedTime))
        ) {
            //Mostramos la primera vez, o cada 1 hora como máximo
            Utils.showStickyMessage(this, appNews, Style.INFO);
            //msgInfo.setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_LONG));
            Utils.addPrefs(this, Constants.USER_PREFERENCE_APP_NEWS_SHOWED_TIME, new Date().getTime());
        }
    }

    protected boolean showedInLastHour(long appNewsShowed) {
        long elapsed = new Date().getTime() - appNewsShowed;
        Log.d(LOG_TAG, String.format("se registraron %s segundos de la última muestra de noticias.",
                elapsed / 1000));
        return elapsed < Constants.ONE_HOUR;
    }
}
