package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;

import io.github.jokoframework.mboehaolib.mark.MyMarkView;

import io.github.jokoframework.datacharts.FloatDataPair;
import io.github.jokoframework.R;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaquin on 23/08/17.
 */

public class LineChartActivity extends Activity {

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
        data.add(new FloatDataPair(0f,15000f));
        data.add(new FloatDataPair(0.1f,17500f));
        data.add(new FloatDataPair(0.2f,16500f));
        data.add(new FloatDataPair(0.3f,18500f));
        data.add(new FloatDataPair(0.4f,20500f));

        // The Chart whe are gonna use...
        LineChart lineChart = findViewById(R.id.line_chart);

        //Configs...
        Description desc = new Description();
        desc.setText(getString(R.string.chart_description));
        desc.setTextColor(R.color.white);
        lineChart.setMarker(new MyMarkView(this));
        lineChart.setDrawMarkers(true);
        lineChart.setDescription(desc);
        lineChart.setBackgroundColor(getResources().getColor(R.color.white));
        lineChart.animateX(3000);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);

        //More configs to Axis representation...
        setFormatAxis(lineChart);

        // insertion of the entries ...
        dataChartInsertion(data, lineChart,this); // data introduccio & styling,others...

    }

    public void dataChartInsertion(List<FloatDataPair> dataObjects, LineChart chart, Context context){
        List<Entry> entries = new ArrayList<>();

        for (FloatDataPair data : dataObjects) {
            entries.add(new Entry(data.getX(), data.getY()));// The constructer gives you the chance to add a Drawable icon...
        }

        LineDataSet dataSet = new LineDataSet(entries, "Testing Chart");// add entries to dataset
        dataSet.setColors(new int[] { R.color.group_divider_color, R.color.colorPrimary}, context);
        dataSet.setHighlightEnabled(true);
        dataSet.setCircleColors(new int[] {R.color.group_divider_color},context);
        dataSet.setCircleRadius(0.4f);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setLineWidth(0.5f);
        List<Integer> colorText = new ArrayList();
        colorText.add(R.color.progress_bar);
        dataSet.setValueTextColors(colorText);


        List<ILineDataSet> dataSets = new ArrayList<>(); // if it must be more than 1 dataset...
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);
        chart.invalidate(); // refresh, could be a sync button...
    }

    public void setFormatAxis(LineChart mLineChart){
        // Eje X...
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        // Eje Y...
        //Right
        YAxis yAxisR = mLineChart.getAxisRight();
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawLabels(false);
        //Left
        YAxis yAxisL = mLineChart.getAxisLeft();
        yAxisL.setDrawAxisLine(true);
        yAxisL.setDrawGridLines(false);

        mLineChart.setDrawGridBackground(false);
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

}
