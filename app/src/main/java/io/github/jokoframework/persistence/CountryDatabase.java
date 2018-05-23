package io.github.jokoframework.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Country.class},version = 1)
public abstract class CountryDatabase extends RoomDatabase {

    private static CountryDatabase INSTANCE;

    public abstract CountryDao countryDao();

    public static CountryDatabase getAppDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CountryDatabase.class, "country_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
