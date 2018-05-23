package io.github.jokoframework.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.github.jokoframework.R;
import io.github.jokoframework.persistence.CountryDatabase;
import io.github.jokoframework.persistence.Country;
import io.github.jokoframework.persistence.DatabaseHandler;
import io.github.jokoframework.databinding.ActivityCountryBinding;

public class CountryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCountryBinding activityCountryBinding = DataBindingUtil.setContentView(this, R.layout.activity_country);

        //Clearing old records from database
        CountryDatabase.getAppDataBase(this).countryDao().deleteAll();

        // Adding User to database
        activityCountryBinding.addCountry
                .setOnClickListener(view -> {
                    Country country = new Country();
                    country.setCountryName(activityCountryBinding.textInputEditText1.getText().toString());
                    country.setCountryCode(activityCountryBinding.textInputEditText2.getText().toString());
                    DatabaseHandler.populateAsync(CountryDatabase.getAppDataBase(CountryActivity.this), country);

                    activityCountryBinding.textInputEditText1.getText().clear();
                    activityCountryBinding.textInputEditText2.getText().clear();

                    Toast.makeText(CountryActivity.this, "Country Added",Toast.LENGTH_SHORT).show();

                });


        // show all user from database
        activityCountryBinding.showCountries
                .setOnClickListener(view -> {
                    List<Country> countryList = CountryDatabase.getAppDataBase(CountryActivity.this).countryDao().getAll();
                    for (int i = 0; i < countryList.size(); i++) {
                        View tableRow = LayoutInflater.from(CountryActivity.this).inflate(R.layout.table_item, null, false);
                        TextView cID = tableRow.findViewById(R.id.cid);
                        TextView countryName = tableRow.findViewById(R.id.countryName);
                        TextView countryCode = tableRow.findViewById(R.id.countryCode);

                        cID.setText(String.valueOf(countryList.get(i).getCid()));
                        countryName.setText(countryList.get(i).getCountryName());
                        countryCode.setText(countryList.get(i).getCountryCode());
                        activityCountryBinding.tableLayout.addView(tableRow);
                    }
                });



    }

    @Override
    protected void onDestroy() {
        CountryDatabase.destroyInstance();
        super.onDestroy();
    }
}
