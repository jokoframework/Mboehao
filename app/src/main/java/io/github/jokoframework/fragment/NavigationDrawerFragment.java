package io.github.jokoframework.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import io.github.jokoframework.R;
import io.github.jokoframework.activity.AboutActivity;
import io.github.jokoframework.activity.BarChartActivity;
import io.github.jokoframework.activity.ChangePasswordActivity;
import io.github.jokoframework.activity.HorizontalBarChartActivity;
import io.github.jokoframework.activity.LineChartActivity;
import io.github.jokoframework.activity.LogOutActivity;
import io.github.jokoframework.activity.MultipleLineChartActivity;
import io.github.jokoframework.adapter.CustomExpandableListAdapter;
import io.github.jokoframework.mboehaolib.constants.Constants;
import io.github.jokoframework.mboehaolib.pojo.Event;
import io.github.jokoframework.mboehaolib.pojo.EventParent;
import io.github.jokoframework.mboehaolib.util.Utils;

/**
 * Created by joaquin on 23/08/17.
 * @author joaquin
 * @author afeltes
 */

public class NavigationDrawerFragment extends Fragment {

    private static final String EVENTS = "events";
    private static final String LOG_TAG = NavigationDrawerCallbacks.class.getSimpleName();

    private static final long MENU_ID_IMAGE1 = 1L;
    private static final long MENU_ID_IMAGE2 = 2L;
    private static final long MENU_ID_IMAGE3 = 3L;
    private static final long MENU_ID_IMAGE4 = 4L;
    private static final long MENU_ID_LOGOUT = 5L;
    private static final long MENU_ID_HELP = 6L;
    private static final long MENU_ID_CHANGEPASS = 8L;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    public View header;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private List<Event> mEvents;
    private List<EventParent> mEventParents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(Constants.PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) { // if activity existed before, then...
            mFromSavedInstanceState = true;
            mEvents = (List<Event>) savedInstanceState.getSerializable(EVENTS);
        }

        mEventParents = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        header = getSideMenuHeader(inflater, container);
        mDrawerListView.addHeaderView(header);
        initializeEvents();
        addDrawerItems();
        return mDrawerListView;
    }

    private void addDrawerItems() {
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this.getActivity(), mEventParents);

        ExpandableListView mExpandableListView = mDrawerListView.findViewById(R.id.menuList);
        mExpandableListView.setAdapter(adapter);

        // Expandir todos los grupos por default...
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mExpandableListView.expandGroup(i);
        }

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // Se maneja el evento por hijo...
                selectGroupItem(groupPosition, childPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                int childrenCount = expandableListView.getExpandableListAdapter().getChildrenCount(groupPosition);
                // Solo es es un grupo sin hijos
                // va a tener una accion asociada
                if (childrenCount == 0) {
                    selectGroupItem(groupPosition, null);
                }
                return false;
            }
        });
    }

    private View getSideMenuHeader(LayoutInflater inflater, ViewGroup container) {
        ParseUser currentUserParse = ParseUser.getCurrentUser();

        View header = inflater.inflate(R.layout.side_menu_header, container, false);
        //Implementa la cabecera del menu...
        TextView welcomeString = (TextView) header.findViewById(R.id.personalize_welcome);

        if (Utils.getPrefs(getActivity(), Constants.FACEBOOK_PROFILE_DATA) == null) {
            if (currentUserParse != null) {
                welcomeString.setText(String.format("Bienvenido %s", currentUserParse.getUsername()));
            } else {
                welcomeString.setText(String.format("Bienvenido a Mboehao"));
            }
        } else {
            welcomeString.setText(String.format("Bienvenido %s", Utils.getPrefs(getActivity(), Constants.FACEBOOK_PROFILE_DATA)));
        }
        return header;
    }

/*
    *   Menu...*/

    /**
     * > Charts...
     * ---> LineChart
     * ---> BarChart
     * ---> HorizontalBarChart
     * ---> MultipleLineChart
     * > Options
     * > Help
     * > Salir
     */

    public void initializeEvents() {

        if (mEvents == null || !mEvents.isEmpty()) {
            //Constructor de los Grupos o Elementos Padres
            //1.Images...
            buildChartsGroup();

            // 2.Options...
            Event passwordChangeEvent = new Event(MENU_ID_CHANGEPASS);
            passwordChangeEvent.setDescription(getString(R.string.changePasswordDescription));
            passwordChangeEvent.setActivity(ChangePasswordActivity.class);
            passwordChangeEvent.setIconMenu(R.drawable.picture);
            mEventParents.add(new EventParent(passwordChangeEvent));

            // 3. Help
            Event helpEvent = new Event(MENU_ID_HELP);
            helpEvent.setDescription(getString(R.string.aboutDescription));
            helpEvent.setActivity(AboutActivity.class);
            helpEvent.setIconMenu(R.drawable.picture);
            mEventParents.add(new EventParent(helpEvent));

            // 4. LogOUT
            Event exitEvent = new Event(MENU_ID_LOGOUT);
            exitEvent.setDescription(getString(R.string.action_logout));
            exitEvent.setActivity(LogOutActivity.class);
            exitEvent.setIconMenu(R.drawable.picture);
            mEventParents.add(new EventParent(exitEvent));
        }
    }

    private void buildChartsGroup() {
        List<Event> eventsList = new ArrayList<>();

        // 1. LineChart...
        Event firstChartEvent = new Event(MENU_ID_IMAGE1);
        firstChartEvent.setDescription(getString(R.string.line_chart));
        firstChartEvent.setActivity(LineChartActivity.class);
        firstChartEvent.setIconMenu(R.drawable.picture);
        eventsList.add(firstChartEvent);

        // 2. BarChart...
        Event secondChartEvent = new Event(MENU_ID_IMAGE2);
        secondChartEvent.setDescription(getString(R.string.bar_chart));
        secondChartEvent.setActivity(BarChartActivity.class);
        secondChartEvent.setIconMenu(R.drawable.picture);
        eventsList.add(secondChartEvent);

        // 3. HorizontalBarChart...
        Event thirdChartEvent = new Event(MENU_ID_IMAGE3);
        thirdChartEvent.setDescription(getString(R.string.horizontal_bar_chart));
        thirdChartEvent.setActivity(HorizontalBarChartActivity.class);
        thirdChartEvent.setIconMenu(R.drawable.picture);
        eventsList.add(thirdChartEvent);

        // 4. MultipleLineChart
        Event fourthChartEvent = new Event(MENU_ID_IMAGE4);
        fourthChartEvent.setDescription(getString(R.string.multiple_line_chart));
        fourthChartEvent.setActivity(MultipleLineChartActivity.class);
        fourthChartEvent.setIconMenu(R.drawable.picture);
        eventsList.add(fourthChartEvent);

        mEventParents.add(new EventParent(getString(R.string.parent_charts), eventsList));
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new SimpleActionBarDrawerToggle(getActivity(), mDrawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void selectGroupItem(int groupPosition, Integer childPosition) {
        EventParent parent = mEventParents.get(groupPosition);
        Event eObject;
        if (childPosition != null) {
            // Click sobre un sub-item
            eObject = mEventParents.get(groupPosition).getEvents().get(childPosition);

            if (mDrawerListView != null) {
                mDrawerListView.setItemChecked(childPosition, true);
            }
        } else {
            // Click sobre un item sin sub-items
            eObject = parent.getEvent();
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }

        if (mCallbacks != null && mDrawerListView != null) {
            if (eObject instanceof Event) {
                Event menuEvent = (Event) eObject;
                mCallbacks.navigationDrawerMenuSelected(menuEvent);
            } else {
                Log.e("ERROR", "El item seleccionado no es un MenuEvent");
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Activity must implement NavigationDrawerCallbacks.");
            throw e;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mEvents != null) {
            //BEGIN-IGNORE-SONARQUBE
            ArrayList<Event> events = new ArrayList<>(mEvents);
            outState.putSerializable(EVENTS, events);
            //END-IGNORE-SONARQUBE
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() { // getActivity() in a Fragment returns the Activity the Fragment is currently associated with
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    @FunctionalInterface
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an menu item in the navigation drawer is selected.
         */
        void navigationDrawerMenuSelected(Event event);
    }

    public boolean isDrawerOpen() {
        /**
         *Retorna true si el navigation drawer esta abierto
         */
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    private class SimpleActionBarDrawerToggle extends ActionBarDrawerToggle {
        public SimpleActionBarDrawerToggle(Activity activity, DrawerLayout mDrawerLayout, int icDrawer, int navigationDrawerOpen, int navigationDrawerClose) {
            super(activity, mDrawerLayout, icDrawer, navigationDrawerOpen, navigationDrawerClose);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            if (!isAdded()) {
                return;
            }

            getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            if (!isAdded()) {
                return;
            }

            if (!mUserLearnedDrawer) {
                // The user manually opened the drawer; store this flag to prevent auto-showing
                // the navigation drawer automatically in the future.
                mUserLearnedDrawer = true;
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(getActivity());
                sp.edit().putBoolean(Constants.PREF_USER_LEARNED_DRAWER, true).apply();
            }
            getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
        }
    }
}

