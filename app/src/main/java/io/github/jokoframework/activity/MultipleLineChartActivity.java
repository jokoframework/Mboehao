package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.github.jokoframework.R;
import io.github.jokoframework.datacharts.FloatDataPair;
import io.github.jokoframework.dialog.ContentChooserDialog;
import io.github.jokoframework.mboehaolib.mark.MyMarkView;

/**
 * Created by Alejandro on 08/06/18.
 */
public class MultipleLineChartActivity extends Activity {

    private static final String LOG_TAG = MultipleLineChartActivity.class.getSimpleName();
    private Spinner mSpinner;
    private LineChart mlineChart;
    private HashMap<String, List<FloatDataPair>> mMultipleChartData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplelinechart);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // The Chart whe are gonna use...
        mlineChart = (LineChart) findViewById(R.id.multiple_line_chart);
        mSpinner = (Spinner) findViewById(R.id.content_chooser_spinner);
        mMultipleChartData = new HashMap<String, List<FloatDataPair>>();
        loadMultipleChartData();
        initializeSpinner();
        initializeChart();
        displayDialog();

    }

    private void loadMultipleChartData() {
        List<String> contentLabels = Arrays.asList(getResources().getStringArray(
                R.array.test_label_array));


        List<FloatDataPair> data1 = getFloatDataPairs();;
        List<FloatDataPair> data2 = getFloatDataPairs();;
        List<FloatDataPair> data3 = getFloatDataPairs();;
        List<FloatDataPair> data4 = getFloatDataPairs();;
        List<FloatDataPair> data5 = getFloatDataPairs();;
        List<FloatDataPair> data6 = getFloatDataPairs();;

        mMultipleChartData.put(contentLabels.get(0), data1);
        mMultipleChartData.put(contentLabels.get(1), data2);
        mMultipleChartData.put(contentLabels.get(2), data3);
        mMultipleChartData.put(contentLabels.get(3), data4);
        mMultipleChartData.put(contentLabels.get(4), data5);
        mMultipleChartData.put(contentLabels.get(5), data6);
    }

    private List<FloatDataPair> getFloatDataPairs() {
        Random random = new Random(System.currentTimeMillis());
        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();
        data.add(new FloatDataPair(0f,Float.valueOf(random.nextInt(20000))));
        data.add(new FloatDataPair(0.1f,Float.valueOf((random.nextInt(20000)))));
        data.add(new FloatDataPair(0.2f,Float.valueOf((random.nextInt(20000)))));
        data.add(new FloatDataPair(0.3f,Float.valueOf((random.nextInt(20000)))));
        data.add(new FloatDataPair(0.4f,Float.valueOf((random.nextInt(20000)))));
        return data;
    }

    private void initializeSpinner() {
        List<String> contentLabels = Arrays.asList(getResources().getStringArray(
                R.array.test_label_array));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contentLabels);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                graphChart(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void initializeChart() {

        //Configs...
        mlineChart.setMarker(new MyMarkView(this));
        mlineChart.setDrawMarkers(true);
        mlineChart.setBackgroundColor(getResources().getColor(R.color.white));
        mlineChart.animateX(3000);
        mlineChart.setHighlightPerDragEnabled(false);
        mlineChart.setHighlightPerTapEnabled(true);

        //More configs to Axis representation...
        setFormatAxis(mlineChart);
    }

    private void displayDialog() {
        List<String> contentLabels = Arrays.asList(getResources().getStringArray(
                R.array.test_label_array));

        ContentChooserDialog mSpinnerDialog = new ContentChooserDialog(this, contentLabels,
                new ContentChooserDialog.DialogListener() {
                    public void cancelled() {
                        // do your code here
                        Log.d(LOG_TAG, "No content selected");
                    }

                    public void ready(int idSelected) {
                        graphChart(idSelected);
                        mSpinner.setSelection(idSelected);
                    }
                });
        mSpinnerDialog.show();
    }

    private void graphChart(int id) {
        String label = (String) mMultipleChartData.keySet().toArray()[id];
        List<FloatDataPair> data = (List<FloatDataPair>) mMultipleChartData.get(label);

        //Configs...
        Description desc = new Description();
        desc.setText(label);
        mlineChart.setDescription(desc);
        // insertion of the entries ...
        dataChartInsertion(data, mlineChart,this); // data introduccio & styling,others...
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
