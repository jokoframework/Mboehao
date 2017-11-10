package io.github.jokoframework.mboehaolib.mark;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import io.github.jokoframework.mboehaolib.R;

/**
 * Created by joaquin on 12/09/17.
 */

public class MyDialogMarkView extends MarkerView {

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */

    private TextView textContent;
    private ImageView iconDraw;

    public MyDialogMarkView(Context context) {
        super(context, R.layout.marker_dialog);

        // find your layout components
        textContent = (TextView) findViewById(R.id.textmarkerContent);
        iconDraw = (ImageView)findViewById(R.id.dialogmarkerImage);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        textContent.setText("This is where you put your EntryData!!");
        iconDraw.setImageResource(R.drawable.info);
        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-getWidth(), -(getHeight() / 2));
        }

        return mOffset;
    }
}
