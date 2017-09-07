package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.androidplot.ui.TableModel;
import com.example.simplerel.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaquin on 23/08/17.
 */

public class FirstImageActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();
        data.add(new FloatDataPair(0.5f,5000f));
        data.add(new FloatDataPair(0.75f,6500f));
        data.add(new FloatDataPair(1.5f,8500f));

        // The Chart whe are gonna use...
        LineChart lineChart = findViewById(R.id.line_chart);

        //Configs...
        Description desc = new Description();
        desc.setText(getString(R.string.chart_description));
        desc.setTextColor(R.color.colorAccent);
        lineChart.setDescription(desc);
        lineChart.setBackgroundColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        lineChart.animateX(3000);
//        lineChart.setMarker(new MarkerImage(this,R.drawable.check));

        //More configs to Axis representation...
        setFormatAxis(lineChart);

        // insertion of the entries ...
        dataChartInsertion(data, lineChart); // data introduccio & styling,others...

    }

    public void dataChartInsertion(List<FloatDataPair> dataObjects, LineChart chart){

        List<Entry> entries = new ArrayList<>();
        Resources res = getResources();
        Drawable icon = res.getDrawable(R.drawable.picture);

        for (FloatDataPair data : dataObjects) {
            entries.add(new Entry(data.getValueX(), data.getValueY(),icon));// The constructer gives you the chance to add a Drawable icon...
        }

        LineDataSet dataSet = new LineDataSet(entries, "Testing Chart");// add entries to dataset
        dataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        dataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        dataSet.setLineWidth(3f);
        dataSet.setValueTextColor(ColorTemplate.getHoloBlue());

        List<ILineDataSet> dataSets = new ArrayList<>(); // if it must be more than 1 dataset...
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        chart.setDrawGridBackground(false);
        chart.setData(lineData);
        chart.invalidate(); // refresh, could be a sync button...
    }


    class FloatDataPair {
        final float x;
        final float y;

        FloatDataPair(float x, float y) {this.x=x;this.y=y;}

        float getValueX(){return this.x;}
        float getValueY(){return this.y;}
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Se obtiene el id del item del action bar seleccionado
         * y se realiza la acción de acuerdo a éste
         */
        if (item.getItemId() == android.R.id.home) {
            backToHome();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setFormatAxis(LineChart mLineChart){
        final String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4" };

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }

        };

        XAxis xAxis = mLineChart.getXAxis();
        // Eje X...
        xAxis.setTextColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(formatter);
    }

    public interface OnChartGestureListener {

        /**
         * Callbacks when a touch-gesture has started on the chart (ACTION_DOWN)
         *
         * @param me
         * @param lastPerformedGesture
         */
        void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture);

        /**
         * Callbacks when a touch-gesture has ended on the chart (ACTION_UP, ACTION_CANCEL)
         *
         * @param me
         * @param lastPerformedGesture
         */
        void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture);

        /**
         * Callbacks when the chart is longpressed.
         *
         * @param me
         */
        public void onChartLongPressed(MotionEvent me);

        /**
         * Callbacks when the chart is double-tapped.
         *
         * @param me
         */
        public void onChartDoubleTapped(MotionEvent me);

        /**
         * Callbacks when the chart is single-tapped.
         *
         * @param me
         */
        public void onChartSingleTapped(MotionEvent me);

        /**
         * Callbacks then a fling gesture is made on the chart.
         *
         * @param me1
         * @param me2
         * @param velocityX
         * @param velocityY
         */
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY);

        /**
         * Callbacks when the chart is scaled / zoomed via pinch zoom gesture.
         *
         * @param me
         * @param scaleX scalefactor on the x-axis
         * @param scaleY scalefactor on the y-axis
         */
        public void onChartScale(MotionEvent me, float scaleX, float scaleY);

        /**
         * Callbacks when the chart is moved / translated via drag gesture.
         *
         * @param me
         * @param dX translation distance on the x-axis
         * @param dY translation distance on the y-axis
         */
        public void onChartTranslate(MotionEvent me, float dX, float dY);
    }
}
