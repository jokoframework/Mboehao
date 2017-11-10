package io.github.jokoframework.mboehaolib.db;

import android.provider.BaseColumns;


public class Diary {

    private static final String[] ALL_COLUMNS;

    static {
        ALL_COLUMNS = new String[]{DaybookEntry.COLUMN_NAME_DAYBOOK_ID,
                DaybookEntry.COLUMN_NAME_AVERAGE_SECONDS,
                DaybookEntry.COLUMN_NAME_CATEGORY,
                DaybookEntry.COLUMN_NAME_FIRST_TIMESTAMP,
                DaybookEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP,
                DaybookEntry.COLUMN_NAME_LAST_TIMESTAMP,
                DaybookEntry.COLUMN_NAME_ROW_COUNT,
                DaybookEntry.COLUMN_NAME_TOTAL_SECONDS
        };
    }

    /* Inner class that defines the table contents */
    public static abstract class DaybookEntry implements BaseColumns {
        public static final String TABLE_NAME = "daybook";
        public static final String COLUMN_NAME_DAYBOOK_ID = "daybook_id";
        public static final String COLUMN_NAME_AVERAGE_SECONDS = "average_seconds";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_FIRST_TIMESTAMP = "first_timestamp";
        public static final String COLUMN_NAME_INSERTED_AT_TIMESTAMP = "inserted_at_timestamp";
        public static final String COLUMN_NAME_LAST_TIMESTAMP = "last_timestamp";
        public static final String COLUMN_NAME_ROW_COUNT = "row_count";
        public static final String COLUMN_NAME_TOTAL_SECONDS = "total_count";
        public static final String QUERY_AVERAGE_DAYBOOK_BY_DAY = "select category, " +
                "avg(average_seconds) as average, avg(row_count) as row_count from daybook " +
                " where inserted_at_timestamp >= ? " +
                " and  inserted_at_timestamp < ? " +
                " group by(category);";

        private DaybookEntry() {
        }
    }

    public static String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    private Diary() {
    }
}
