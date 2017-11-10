package io.github.jokoframework.mboehaolib.db;

import android.provider.BaseColumns;

/**
 * Created by joaquin on 03/10/17.
 */

public class UsageStat {
    private static final String[] ALL_COLUMNS;

    static {
        /* Colocamos primero el ID y luego en orden alfabético */
        ALL_COLUMNS = new String[]{
                UsageStatEntry.COLUMN_NAME_USAGE_STAT_ID,
                UsageStatEntry.COLUMN_NAME_APP_PACKAGE,
                UsageStatEntry.COLUMN_NAME_CATEGORY,
                UsageStatEntry.COLUMN_NAME_FIRST_TIMESTAMP,
                UsageStatEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP,
                UsageStatEntry.COLUMN_NAME_LAST_TIME_USED,
                UsageStatEntry.COLUMN_NAME_LAST_TIMESTAMP,
                UsageStatEntry.COLUMN_NAME_TOTAL_TIME_FOREGROUND
        };
    }

    /* Inner class that defines the table contents */
    public static abstract class UsageStatEntry implements BaseColumns {
        public static final String TABLE_NAME = "usage_stat";
        public static final String COLUMN_NAME_USAGE_STAT_ID = "usage_stat_id";
        public static final String COLUMN_NAME_APP_PACKAGE = "app_package";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_FIRST_TIMESTAMP = "first_timestamp";
        public static final String COLUMN_NAME_LAST_TIME_USED = "last_time_used";
        public static final String COLUMN_NAME_LAST_TIMESTAMP = "last_timestamp";
        public static final String COLUMN_NAME_TOTAL_TIME_FOREGROUND = "total_time_foreground";
        public static final String COLUMN_NAME_INSERTED_AT_TIMESTAMP = "inserted_at_timestamp";
        public static final String QUERY_USAGE_STATS_GROUP_BY_CATEGORY_ =
                "select (sum(total_time_foreground)/1000) as total_seconds, " +
                        "(avg(total_time_foreground)/1000) as average_seconds, " +
                        "count(1) as row_count, category, " +
                        "min(inserted_at_timestamp) begin_time, " +
                        "max(inserted_at_timestamp) end_time from usage_stat " +
                        "where inserted_at_timestamp >= ? " +
                        "and inserted_at_timestamp < ?  " +
                        "group by (category) " +
                        "order by average_seconds desc";


        private UsageStatEntry() {
        }
    }

    public static String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    private UsageStat() {
    }

}
