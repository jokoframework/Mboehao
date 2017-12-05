package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.jokoframework.R;
import io.github.jokoframework.fragment.NavigationDrawerFragment;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.pojo.Event;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.service.TestServiceNotification;


public class HomeActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static final String TAG = FirebaseInstanceIdService.class.getSimpleName();
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onResume() {
        super.onResume();
        showAppNews();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WebView webView = this.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient(this));
        displayWebView(webView); //display de webUrl after set some settings of the WebView...
        startAlarmServices(this);
        sendToken();
    }

    private void sendToken(){
        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log,Send and toast the Token...
        String msg = String.format("%s - %s",R.string.msg_token_fmt, token);
        sendMessage(token);
        Log.d(TAG, msg);
    }

    public void sendMessage(String msg){
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(String.format("%s@gcm.googleapis.com",Constants.SENDER_ID))
                .setMessageId(String.format("%d",(Constants.msgId + 1)))
                .addData("token", msg)
                .build());
    }

    private class MyWebViewClient extends WebViewClient {
        private final View progressBarView = findViewById(R.id.progressHomeWindow);         // progress bar Parent...
        private WebView webView = (WebView)findViewById(R.id.webview);
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
                if(progressBarView != null && progressBarView.isActivated()){
                    progressBarView.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                }
            } catch (Exception exception) {
                Log.e(LOG_TAG,getString(R.string.errorLoadingPage),exception);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_home_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        if (item.getItemId() == R.id.menu_item_share) {
            Intent share = new Intent(HomeActivity.this, ShareActivity.class);
            startActivity(share);
            finish();
        }
        if (item.getItemId() == R.id.menu_item_preferences) {
            Intent pref = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(pref);
            finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void navigationDrawerMenuSelected(Event event) {
        Intent intent = new Intent(this, event.getActivity());
        //Si el evento es Login, que es el que se asimila al Logout...
        if (event.getActivity().equals(LoginActivity.class)) {
            //Se quita del parseUser y ademas vuelve a mostrar la pantalla de Login por si quiera volver a entrar...
            doLogout(intent);
        }else {
            startActivity(intent);
            // Si no es igual lanza la actividad, que podria ser mostrar alguns de las imagenes o alguna otra opcion...
        }
    }

    private void doLogout(Intent intent) {
        ParseUser.logOut();
        cancelAlarmServices(this);
        startActivity(intent);
        finish();
    }

    private void setupNavigationDrawerFragment() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.menuFragment);
        // It might happen while rotating that   ?¿...
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.setUp(R.id.menuFragment, (DrawerLayout) findViewById(R.id.drawer_layout));
        }
    }

    public void loadData() {
        setupNavigationDrawerFragment();
    }


    public static class MainViewFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_show_info, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final HomeActivity activity =  (HomeActivity) getActivity();
            activity.loadData();
        }
    }

    private void displayWebView(WebView webView){
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
