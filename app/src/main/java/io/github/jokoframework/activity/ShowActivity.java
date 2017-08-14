package io.github.jokoframework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplerel.R;
import com.parse.ParseUser;

import io.github.jokoframework.fragment.NavigationDrawerFragment;
import io.github.jokoframework.pojo.Event;


public class ShowActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Se crea el action bar y se muestra solo en el Show cuando el
         * navigation drawer esta cerrado
         */
        if (mNavigationDrawerFragment == null || !mNavigationDrawerFragment.isDrawerOpen()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_home_menu_bar, menu);
            return true;
        } else {
            return false;
        }

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

    public static class MainViewFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_show_info, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final ShowActivity activity =  (ShowActivity) getActivity();
            activity.loadData();
        }

    }

    private void setupNavigationDrawerFragment() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.menuFragment);
        // It might happen while rotating that   ?¿...
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.setUp(
                    R.id.menuFragment,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }
    }


    public void loadData() {
        setupNavigationDrawerFragment();
    }

}
