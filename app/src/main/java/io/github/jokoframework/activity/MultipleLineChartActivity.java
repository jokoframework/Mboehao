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
import java.util.List;

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
        initializeSpinner();
        initializeChart();

    }

    private void initializeSpinner() {
        List<String> contentLabels = new ArrayList<>();
        contentLabels.add("Contenido 1");
        contentLabels.add("Contenido 2");
        contentLabels.add("Contenido 3");
        contentLabels.add("Contenido 4");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contentLabels);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                graphChart(i);
            }
        });
        displayDialog();
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
        List<String> contentLabels = new ArrayList<>();
        contentLabels.add("Contenido 1");
        contentLabels.add("Contenido 2");
        contentLabels.add("Contenido 3");
        contentLabels.add("Contenido 4");

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
    }

    private void graphChart(int idSelected) {

        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();
        data.add(new FloatDataPair(idSelected*0f,15000f));
        data.add(new FloatDataPair(idSelected*0.1f,17500f));
        data.add(new FloatDataPair(idSelected*0.2f,16500f));
        data.add(new FloatDataPair(idSelected*0.3f,18500f));
        data.add(new FloatDataPair(idSelected*0.4f,20500f));

        //Configs...
        Description desc = new Description();
        desc.setText("Contenido " + (Integer.toString(idSelected)));
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
