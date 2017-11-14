package io.github.jokoframework.mark;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;

import io.github.jokoframework.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by joaquin on 11/09/17.
 */

public class MyMarkView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    private TextView textContent;
    private ImageView iconDraw;

    public MyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        // find your layout components
        textContent = (TextView) findViewById(R.id.markerContent);
        iconDraw = (ImageView)findViewById(R.id.markerImage);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textContent.setText(String.format("%s, %s" , e.getY(), e.getX()));
        iconDraw.setImageResource(R.drawable.bubble);
            // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
