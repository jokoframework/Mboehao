package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.simplerel.R;
import com.parse.ParseUser;

import io.github.jokoframework.fragment.NavigationDrawerFragment;
import io.github.jokoframework.pojo.Event;


public class HomeActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient(this));
        displayWebView(webView); //display de webUrl after set some settings of the WebView...
    }

    private class MyWebViewClient extends WebViewClient {
        final View progressBarView = findViewById(R.id.progressHomeWindow);         // progress bar Parent...
        WebView webView = (WebView)findViewById(R.id.webview);
        Activity activity;
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
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

}
