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

import com.example.simplerel.R;

import java.util.ArrayList;
import java.util.List;

import io.github.jokoframework.activity.SecondImageActivity;
import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.pojo.Event;
import io.github.jokoframework.pojo.EventParent;
import io.github.jokoframework.adapter.CustomExpandableListAdapter;
import io.github.jokoframework.activity.LoginActivity;
import io.github.jokoframework.activity.FirstImageActivity;

/**
 * Created by joaquin on 23/08/17.
 */

public class NavigationDrawerFragment extends Fragment {

    private static final String EVENTS = "events";
    private static final String LOG_TAG = NavigationDrawerCallbacks.class.getSimpleName();

    private static final long MENU_ID_IMAGE1 = 1L;
    private static final long MENU_ID_IMAGE2 = 2L;
    private static final long MENU_ID_LOGOUT = 3L;

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
        View header = getSideMenuHeader(inflater, container);
        mDrawerListView.addHeaderView(header);
        initializeEvents();
        addDrawerItems();
        return mDrawerListView;
    }

    private void addDrawerItems() {
        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(this.getActivity(), mEventParents);

        ExpandableListView mExpandableListView = (ExpandableListView) mDrawerListView.findViewById(R.id.menuList);
        mExpandableListView.setAdapter(adapter);

        // Expand all groups by default
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mExpandableListView.expandGroup(i);
        }

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // Se maneja el evento por hijo
                selectGroupItem(groupPosition, childPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                int childrenCount = expandableListView.getExpandableListAdapter().getChildrenCount(groupPosition);
                // Only if it is a group without children
                // will have an action associated to the click event
                if (childrenCount == 0) {
                    selectGroupItem(groupPosition, null);
                }
                return false;
            }
        });
    }

    private View getSideMenuHeader(LayoutInflater inflater, ViewGroup container) {
        //Implementa la cabecera del menu...
        View header = inflater.inflate(R.layout.side_menu_header, container, false);
        return header;
    }

/*
* Idea del Menu...*/
    /**
     *  > Imagenes
     *  ---> Imagen1
     *  ---> Imagen2
     *  > Salir
     */

    public void initializeEvents() {

        if (mEvents == null || !mEvents.isEmpty()) {
            //Constructur del grupo de imagenes

            buildImageGroup();
            // 2. Salir
            Event exitEvent = new Event(MENU_ID_LOGOUT);
            exitEvent.setDescription(getString(R.string.action_logout));
            exitEvent.setActivity(LoginActivity.class);
            exitEvent.setIconMenu(R.drawable.picture);
            mEventParents.add(new EventParent(exitEvent));
        }
    }

    private void buildImageGroup() {
        List<Event> eventsList = new ArrayList<>();

        // 1. Imagen1...
        Event firstImageEvent = new Event(MENU_ID_IMAGE1);
        firstImageEvent.setDescription(getString(R.string.picture1));
        firstImageEvent.setActivity(FirstImageActivity.class);
        firstImageEvent.setIconMenu(R.drawable.picture);
        eventsList.add(firstImageEvent);

        // 2. Imagen2...
        Event secondImageEvent = new Event(MENU_ID_IMAGE2);
        secondImageEvent.setDescription(getString(R.string.picture2));
        secondImageEvent.setActivity(SecondImageActivity.class);
        secondImageEvent.setIconMenu(R.drawable.picture);
        eventsList.add(secondImageEvent);

        mEventParents.add(new EventParent(getString(R.string.parent_images), eventsList));
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

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new SimpleActionBarDrawerToggle(getActivity(),mDrawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

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
                Log.e("ERROR","El item seleccionado no es un MenuEvent");
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
