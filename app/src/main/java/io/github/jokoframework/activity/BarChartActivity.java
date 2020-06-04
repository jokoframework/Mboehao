package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.ImageView;

import io.github.jokoframework.R;
import com.github.mikephil.charting.charts.BarChart;
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
import io.github.jokoframework.mboehaolib.mark.MyMarkView;


public class BarChartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);

        ImageView backButton = findViewById(R.id.backButton3);

        backButton.setOnClickListener(v -> backToHome());

        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();
        data.add(new FloatDataPair(0,150f));
        data.add(new FloatDataPair(1,130f));
        data.add(new FloatDataPair(2,200f));
        data.add(new FloatDataPair(3,300f));
        data.add(new FloatDataPair(4,473f));

        // The Chart whe are gonna use...
        BarChart barChart = findViewById(R.id.bar_chart);
//        //Configs...
        Description desc = new Description();
        desc.setText(getString(R.string.chart_description));
        desc.setTextColor(R.color.white);
        barChart.setDescription(desc);
        barChart.setBackgroundColor(getResources().getColor(R.color.white));
        barChart.setDrawGridBackground(false);
        barChart.setMarker(new MyMarkView(this));
        barChart.setScaleEnabled(true);
        barChart.animateXY(2000,2000);

//        //More configs to Axis representation...
        setFormatAxis(barChart);

        // insertion of the entries ...
        dataChartInsertion(data, barChart); // data introduccio & styling,others...

    }

    public void dataChartInsertion(List<FloatDataPair> dataObjects, BarChart chart){

        List<BarEntry> entries = new ArrayList<>();

        for (FloatDataPair data : dataObjects) {
            entries.add(new BarEntry(data.getXi(), data.getY()));// The constructer gives you the chance to add a Drawable icon...
        }

        BarDataSet dataSet = new BarDataSet(entries, "Testing BarChart");
        // add entries to dataset
        dataSet.setColors(getResources().getColor(R.color.background_drawer_group),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.drawer_text));
        dataSet.setHighlightEnabled(true);
        dataSet.setValueTextColor(getResources().getColor(R.color.progress_bar));

        List<IBarDataSet> dataSets = new ArrayList<>(); // if it must be more than 1 dataset...
        dataSets.add(dataSet);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // set custom bar width
        chart.setData(barData);

        chart.invalidate(); // refresh, could be a sync button...
    }

    private void setFormatAxis(BarChart mbarChart){
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
        XAxis xAxis = mbarChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new LabelFormatter(labels));

        // Eje Y...
        //Right
        YAxis yAxisR = mbarChart.getAxisRight();
        yAxisR.setDrawAxisLine(false);
        yAxisR.setDrawGridLines(false);
        yAxisR.setDrawLabels(false);
        //Left
        YAxis yAxisL = mbarChart.getAxisLeft();
        yAxisL.setDrawAxisLine(true);
        yAxisL.setDrawGridLines(false);


        mbarChart.setDrawGridBackground(false);
        mbarChart.setFitBars(true); // make the x-axis fit exactly all bars

    }

    private void backToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
