package io.github.jokoframework.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.jokoframework.R;
import io.github.jokoframework.mboehaolib.util.Utils;
import io.github.jokoframework.persistence.CountryDatabase;
import io.github.jokoframework.persistence.Country;

public class CountryActivity extends Activity {

    private static final String LOG_TAG = "COUNTRYACTIVITY";

    public static List<Country> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_country);

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        final Button button = (Button) findViewById(R.id.show_countries);

        //Clearing old records from database
        //CountryDatabase.getAppDataBase(this).countryDao().deleteAll();

        // show all country from database
        tableLayout.removeAllViews();

        if (countryList.size() == 0){
            View tableRow = LayoutInflater.from(CountryActivity.this).inflate(R.layout.table_item, null, false);
            TextView countryName = tableRow.findViewById(R.id.countryName);
            countryName.setText("Sin paises.");
            tableLayout.addView(tableRow);
        } else {
            for (int i = 0; i < countryList.size(); i++) {
                View tableRow = LayoutInflater.from(CountryActivity.this).inflate(R.layout.table_item, null, false);
                TextView cID = tableRow.findViewById(R.id.cid);
                TextView countryName = tableRow.findViewById(R.id.countryName);
                TextView countryCode = tableRow.findViewById(R.id.countryCode);

                cID.setText(String.valueOf(countryList.get(i).getCid()));
                countryName.setText(countryList.get(i).getCountryName());
                countryCode.setText(countryList.get(i).getCountryCode());
                tableLayout.addView(tableRow);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    Intent mServiceIntent = new Intent(getBaseContext(), io.github.jokoframework.service.CountryHelper.class);
                    getBaseContext().startService(mServiceIntent);
                    Utils.showToast(getBaseContext(), String.format("Actualizando lista..."));
                } catch (RuntimeException e) {
                    Utils.showToast(getBaseContext(), String.format("Fallo de CountryHelper"));
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, getBaseContext().getString(R.string.no_network_connection), e);
                }

                tableLayout.removeAllViews();

                if (countryList.size() == 0){
                    View tableRow = LayoutInflater.from(CountryActivity.this).inflate(R.layout.table_item, null, false);
                    TextView countryName = tableRow.findViewById(R.id.countryName);
                    countryName.setText("Sin paises.");
                    tableLayout.addView(tableRow);
                } else {
                    for (int i = 0; i < countryList.size(); i++) {
                        View tableRow = LayoutInflater.from(CountryActivity.this).inflate(R.layout.table_item, null, false);
                        TextView cID = tableRow.findViewById(R.id.cid);
                        TextView countryName = tableRow.findViewById(R.id.countryName);
                        TextView countryCode = tableRow.findViewById(R.id.countryCode);

                        cID.setText(String.valueOf(countryList.get(i).getCid()));
                        countryName.setText(countryList.get(i).getCountryName());
                        countryCode.setText(countryList.get(i).getCountryCode());
                        tableLayout.addView(tableRow);
                    }
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        CountryDatabase.destroyInstance();
        super.onDestroy();
    }
}
