package io.github.jokoframework.mboehaolib.db;


import android.provider.BaseColumns;

public class BaseLine {

    private static final String[] ALL_COLUMNS;

    static {
        /* Colocamos primero el ID y luego en orden alfabético */
        ALL_COLUMNS = new String[]{
                BaseLineEntry.COLUMN_NAME_BASE_LINE_ID,
                BaseLineEntry.COLUMN_NAME_FIRST_TIME_USAGE_RECORDED,
                BaseLineEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP,
                BaseLineEntry.COLUMN_NAME_PACKAGE_NAME
        };
    }

    /* Inner class that defines the table contents */
    public static abstract class BaseLineEntry implements BaseColumns {
        public static final String TABLE_NAME = "base_line";
        public static final String COLUMN_NAME_BASE_LINE_ID = "base_line_id";
        public static final String COLUMN_NAME_FIRST_TIME_USAGE_RECORDED = "first_usage_timestamp";
        public static final String COLUMN_NAME_INSERTED_AT_TIMESTAMP = "inserted_at_timestamp";
        public static final String COLUMN_NAME_PACKAGE_NAME = "package_name";

        private BaseLineEntry() {
        }
    }

    private BaseLine() {
    }

    public static String[] getAllColumns() {
        return ALL_COLUMNS;
    }
}
