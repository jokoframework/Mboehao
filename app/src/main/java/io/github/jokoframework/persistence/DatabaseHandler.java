package io.github.jokoframework.persistence;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

public class DatabaseHandler {

    public static void populateAsync(@NonNull final CountryDatabase db, Country country){
        PopulateDbAsync task = new PopulateDbAsync(db, country);
        task.execute();
    }

    /* Aysnc Task to insert data to table */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CountryDatabase mDb;
        private final Country newCountry;

        PopulateDbAsync(CountryDatabase db, Country country) {
            mDb = db;
            newCountry = country;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDb.countryDao().insertCountry(newCountry);
            return null;
        }
    }

}
