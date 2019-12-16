package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;

import io.github.jokoframework.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import io.github.jokoframework.datacharts.FloatDataPair;
import io.github.jokoframework.mboehaolib.mark.MyDialogMarkView;

/**
 * Created by joaquin on 12/09/17.
 */

public class HorizontalBarChartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontalbarchart);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();

        data.add(new FloatDataPair(0,110f));
        data.add(new FloatDataPair(1,70f));
        data.add(new FloatDataPair(2,300f));
        data.add(new FloatDataPair(3,220f));
        data.add(new FloatDataPair(4,350f));

        // The Chart whe are gonna use...
        HorizontalBarChart horizontalBarChart = findViewById(R.id.horizontalbar_chart);
        //Configs...
        Description desc = new Description();
        desc.setText(getString(R.string.chart_description));
        desc.setTextColor(R.color.white);
        horizontalBarChart.setDescription(desc);
        horizontalBarChart.setBackgroundColor(getResources().getColor(R.color.white));
        horizontalBarChart.setMarker(new MyDialogMarkView(this)); // se puede hacer varias clases de Marks, segun graficos o topicos...
        horizontalBarChart.setScaleEnabled(true);
        horizontalBarChart.animateXY(2000,2000);

        //More configs to Axis representation...
        setFormatAxis(horizontalBarChart);

        // insertion of the entries ...
        dataChartInsertion(data, horizontalBarChart); // data introduccio & styling,others...

    }

    public void dataChartInsertion(List<FloatDataPair> dataObjects, HorizontalBarChart chart){

        List<BarEntry> entries = new ArrayList<>();

        for (FloatDataPair data : dataObjects) {
            entries.add(new BarEntry(data.getXi(), data.getY()));// The constructer gives you the chance to add a Drawable icon...
        }

        BarDataSet dataSet = new BarDataSet(entries, "Testing HorizontalBarChart");
        // add entries to dataset
        dataSet.setColors(new int[]{getResources().getColor(R.color.background_drawer_group),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.drawer_text)});
        dataSet.setHighlightEnabled(true);
        dataSet.setValueTextColor(getResources().getColor(R.color.progress_bar));

        List<IBarDataSet> dataSets = new ArrayList<>(); // if it must be more than 1 dataset...
        dataSets.add(dataSet);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width
        chart.setData(barData);

        chart.invalidate(); // refresh, could be a sync button...
    }

    private void setFormatAxis(HorizontalBarChart hbarChart){
        String[] labels = new String[] {"08/2017","09/2017","10/2017","11/2017","12/2017"};

        class LabelFormatter implements IAxisValueFormatter {
            private final String[] mLabels;

            public LabelFormatter(String[] labels) {
                mLabels = labels;
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mLabels[(int) value];
            }

        }

        // Eje X...
        XAxis xAxis = hbarChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new LabelFormatter(labels));

        // Eje Y...
        //Right
        YAxis yAxisR = hbarChart.getAxisRight();
        yAxisR.setDrawAxisLine(true);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawLabels(true);
        //Left
        YAxis yAxisL = hbarChart.getAxisLeft();
        yAxisL.setDrawAxisLine(true);
        yAxisL.setDrawGridLines(false);

        hbarChart.setDrawGridBackground(false);
        hbarChart.setFitBars(true); // make the x-axis fit exactly all bars

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
