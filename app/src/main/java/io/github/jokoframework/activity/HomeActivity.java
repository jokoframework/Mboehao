package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.simplerel.R;
import com.parse.ParseUser;

import io.github.jokoframework.fragment.NavigationDrawerFragment;
import io.github.jokoframework.logger.RemoteLogger;
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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getString(R.string.urlWiki));

    }

    private class MyWebViewClient extends WebViewClient {
        private final Activity activity;
        private ProgressDialog progressDialog;// TODO: ProgresDialog change to ProgressBar

        private MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(LOG_TAG, String.format("Loading %s", url));
            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(HomeActivity.this);
                progressDialog.setMessage(getString(R.string.loading_web_page));
                progressDialog.show();
                Log.d(LOG_TAG, String.format("Showing for %s", url));
            }
        }

        public void onPageFinished(WebView view, String url) {
            showFinalResults();
        }

        private void showFinalResults() {
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            } catch (Exception exception) {
                RemoteLogger.e(LOG_TAG,getString(R.string.errorLoadingPage));
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

}
