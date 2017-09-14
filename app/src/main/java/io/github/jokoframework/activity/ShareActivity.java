package io.github.jokoframework.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import com.example.simplerel.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.datacharts.FloatDataPair;
import io.github.jokoframework.mark.MyMarkView;
import io.github.jokoframework.utilitys.Utils;

/**
 * Created by joaquin on 14/09/17.
 */

public class ShareActivity extends FragmentActivity {

    private String shareableFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if(Utils.isNetworkAvailable(this)) {
            setContentView(R.layout.activity_custom_date_rage_plot);
            LineChart lineChartSharing = findViewById(R.id.linechart_sharing);
            initializeChart(lineChartSharing);
        }
    }

    private void initializeChart(LineChart lineChartSharing) {
        View share = findViewById(R.id.imageShareCustomPlot);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getShareIntent());
            }
        });
        setUpChart(lineChartSharing);
    }

    private Intent getShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setType(Constants.IMAGE_PNG);
        sendIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(Utils.getShareImagesFolder(), getShareableFileName())));
        return sendIntent;
    }

    private void setUpChart(LineChart lineChart) {
        //same points of the LineChart example...
        //Points to be in the graph...
        List<FloatDataPair> data = new ArrayList<>();
        data.add(new FloatDataPair(0f,15000f));
        data.add(new FloatDataPair(0.1f,17500f));
        data.add(new FloatDataPair(0.2f,16500f));
        data.add(new FloatDataPair(0.3f,18500f));
        data.add(new FloatDataPair(0.4f,20500f));

        //Configs...
        Description desc = new Description();
        desc.setText(getString(R.string.chart_description));
        desc.setTextColor(R.color.white);
        lineChart.setMarker(new MyMarkView(this, R.layout.marker_tryout));
        lineChart.setDrawMarkers(true);
        lineChart.setDescription(desc);
        lineChart.setBackgroundColor(getResources().getColor(R.color.white));
        lineChart.animateX(3000);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);

        LineChartActivity lChartSetUp = new LineChartActivity();
        lChartSetUp.dataChartInsertion(data,lineChart);
        lChartSetUp.setFormatAxis(lineChart);
    }

    public String getShareableFileName() {
        if (StringUtils.isBlank(shareableFileName)) {
            shareableFileName = Utils.getShareableImageName(Constants.EXTENDED_FILE);
        }
        return shareableFileName;
    }
}
