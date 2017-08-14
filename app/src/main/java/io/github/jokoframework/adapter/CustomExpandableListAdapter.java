package io.github.jokoframework.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import io.github.jokoframework.pojo.*;
import com.example.simplerel.R;

/**
 * Created by joaquin on 23/08/17.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{


    public static final String LOG_TAG = CustomExpandableListAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater;

    private List<EventParent> mEventParents;

    public CustomExpandableListAdapter(Context context, List<EventParent> eventParents) {
        Context mContext = context;
        mEventParents = eventParents;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mEventParents.get(groupPosition).getEvents().get(childPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View converterView, ViewGroup parent) {

        final Event event = (Event) getChild(listPosition, expandedListPosition);
        View currentConvertView = converterView;
        if (event == null) {
            //Acá el event siempre es null
            Log.d(LOG_TAG, String.format("EVENT NULL position: %s", expandedListPosition));
        } else {
            if (currentConvertView == null) {
                currentConvertView = mLayoutInflater.inflate(R.layout.drawer_item_child, null);
            }

            TextView expandedListTextView = (TextView) currentConvertView
                    .findViewById(R.id.drawerText);
            expandedListTextView.setText(event.getDescription());

            ImageView imageView = (ImageView) currentConvertView.findViewById(R.id.imageViewSideMenu);
            imageView.setImageResource(event.getIconMenu());
        }

        return currentConvertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return mEventParents.get(groupPosition).getEvents().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return mEventParents.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mEventParents.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View converterView, ViewGroup parent) {
        EventParent eventParent = (EventParent) getGroup(listPosition);
        View currentConvertView;
        // Performance wise, this should not be commented,
        // but for now we avoid a weird bug of reordering of
        // the menu items
        // http://stackoverflow.com/questions/26530049/expandablelistview-reorganizes-children-views-order-on-every-group-expand
        if (eventParent.getEvent() != null) {
            // Menu drawer item with image and no childs
            Event e = eventParent.getEvent();
            currentConvertView = mLayoutInflater.inflate(R.layout.drawer_item_parent, null);
            TextView txtTitle = (TextView) currentConvertView.findViewById(R.id.drawerText);
            txtTitle.setText(e.getDescription());
        } else {
            currentConvertView = mLayoutInflater.inflate(R.layout.drawer_group, null);
            TextView listTitleTextView = (TextView) currentConvertView
                    .findViewById(R.id.listTitle);
            listTitleTextView.setText(eventParent.getTitle());

        }

        return currentConvertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


}
