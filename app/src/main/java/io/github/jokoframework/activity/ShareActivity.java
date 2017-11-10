package io.github.jokoframework.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.github.jokoframework.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.jokoframework.mboehaolib.constants.Constants;import io.github.jokoframework.datacharts.FloatDataPair;
import io.github.jokoframework.mboehaolib.util.Utils;

import io.github.jokoframework.mboehaolib.mark.MyMarkView;

/**
 * Created by joaquin on 14/09/17.
 */

public class ShareActivity extends FragmentActivity {

    private LineChart lineChartSharing;
    private static final String LOG_TAG = ShareActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utils.isNetworkAvailable(this)) {
            setContentView(R.layout.activity_custom_share_chart);
            try {
                initializeChart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeChart() throws IOException {
        lineChartSharing = findViewById(R.id.linechart_sharing);
        setUpChart(getLineChart());
        if (isExternalStorageWritable()) {
            if (wasImageGeneratedSave()) {
                final View share = findViewById(R.id.imageShareCustomPlot);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(Intent.createChooser(getShareIntent(), "Share images to.."));
                    }
                });
            }else {
                //still do not do anything...
                Toast.makeText(this, String.format("Something went WRONG!"), Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG,"Imagen del Share Activity sin guardar correctamente!");
            }
        }
    }

    private Intent getShareIntent() {
        File datapath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(datapath,String.format("MboehaoApplication-%s.png", Utils.getFormattedDate(new Date())));
        Intent intent = new Intent();
        Uri uri = Uri.fromFile(file);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(Constants.IMAGE_PNG);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        return intent;
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
        desc.setTextColor(getResources().getColor(R.color.black));
        lineChart.setMarker(new MyMarkView(this));
        lineChart.setDrawMarkers(true);
        lineChart.setDescription(desc);
        lineChart.setBackgroundColor(getResources().getColor(R.color.white));
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(true);
        LineChartActivity lChartSetUp = new LineChartActivity();
        lChartSetUp.dataChartInsertion(data,lineChart,this);
        lChartSetUp.setFormatAxis(lineChart);
    }

    private boolean wasImageGeneratedSave() {
        getLineChart().buildDrawingCache();
        Bitmap bitmap = getBitmapFromChart();
        getLineChart().destroyDrawingCache();
        if (bitmap != null) {
            try {
                //Crear la imagen a compartir
                store(bitmap);
                return true;
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        return false;
    }

    private Bitmap getBitmapFromChart() {
        getLineChart().buildDrawingCache();
        getLineChart().setDrawingCacheEnabled(true);
        int width = View.MeasureSpec.makeMeasureSpec(500, View.MeasureSpec.EXACTLY);
        int height = View.MeasureSpec.makeMeasureSpec(700, View.MeasureSpec.EXACTLY);
        getLineChart().measure(width, height);
        int measuredWidth = getLineChart().getMeasuredWidth();
        int measuredHeight = getLineChart().getMeasuredHeight();

        getLineChart().layout(0, 0, measuredWidth, measuredHeight);
        Bitmap chartBitmap = getLineChart().getDrawingCache();
        if (chartBitmap != null) {
            Bitmap chartBitmapResult = Bitmap.createBitmap(chartBitmap.getWidth(), chartBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            // Make a canvas with which we can draw to the chartBitmap
            Canvas canvas = new Canvas(chartBitmapResult);
            canvas.drawColor(0xffffffff);
            canvas.drawBitmap(chartBitmap, 0, 0, null);
            canvas.save();
            return chartBitmapResult;
        } else {
            return null;
        }
    }

    public boolean store(Bitmap bitmap) throws IOException {
        try {
            writeBitmapToFile(this, bitmap);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void writeBitmapToFile(Context context, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        OutputStream os = null;
        try {
            File rootDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFileName = new File(rootDirectory, String.format("MboehaoApplication-%s.png", Utils.getFormattedDate(new Date())));
            if(imageFileName.exists()){
                imageFileName.delete();
            }
            os = new FileOutputStream(imageFileName);
            os.write(byteArray);
            closeStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeStream(Closeable pStream) {
        try {
            if (pStream != null) {
                pStream.close();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "No se pudo cerrar el Stream", e);
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public LineChart getLineChart() {
        return lineChartSharing;
    }

}
