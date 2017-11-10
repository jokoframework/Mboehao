package io.github.jokoframework.mboehaolib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.jokoframework.mboehaolib.logger.RemoteLogger;
import io.github.jokoframework.mboehaolib.db.Diary.DaybookEntry;
import io.github.jokoframework.mboehaolib.db.BaseLine.BaseLineEntry;
import io.github.jokoframework.mboehaolib.db.UsageStat.UsageStatEntry;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = SQLiteHelper.class.getSimpleName();


    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = String.format("simpleplus-v%s.db", DATABASE_VERSION);
    protected Context context;
    private static final String SQL_DELETE_ENTRIES;
    private static SQLiteHelper _instance;

    final String CREATE_TABLE_ACTIVITY = "CREATE TABLE " + DaybookEntry.TABLE_NAME + "(" +
            DaybookEntry.COLUMN_NAME_DAYBOOK_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            DaybookEntry.COLUMN_NAME_AVERAGE_SECONDS + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_FIRST_TIMESTAMP + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_CATEGORY + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_LAST_TIMESTAMP + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_ROW_COUNT + " type TEXT NOT NULL, \n" +
            DaybookEntry.COLUMN_NAME_TOTAL_SECONDS + " type TEXT NOT NULL \n" +
            ");";
    final String CREATE_TABLE_BASE_LINE = "CREATE TABLE " + BaseLineEntry.TABLE_NAME + "(" +
            BaseLineEntry.COLUMN_NAME_BASE_LINE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            BaseLineEntry.COLUMN_NAME_FIRST_TIME_USAGE_RECORDED + " type TEXT NOT NULL, \n" +
            BaseLineEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP + " type TEXT NOT NULL, \n" +
            BaseLineEntry.COLUMN_NAME_PACKAGE_NAME + " type TEXT NOT NULL \n" +
            ");";
    String CREATE_TABLE_USAGE_STAT = "CREATE TABLE " + UsageStatEntry.TABLE_NAME + " ( \n" +
            UsageStatEntry.COLUMN_NAME_USAGE_STAT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            UsageStatEntry.COLUMN_NAME_APP_PACKAGE + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_CATEGORY + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_FIRST_TIMESTAMP + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_INSERTED_AT_TIMESTAMP + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_LAST_TIME_USED + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_LAST_TIMESTAMP + " type TEXT NOT NULL, \n" +
            UsageStatEntry.COLUMN_NAME_TOTAL_TIME_FOREGROUND + " type TEXT NOT NULL \n" +
            "); \n";

    static {
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS ");
        builder.append(DaybookEntry.TABLE_NAME).append(";\n");
        builder.append("DROP TABLE IF EXISTS ");
        builder.append(BaseLineEntry.TABLE_NAME).append(";\n");
        builder.append("DROP TABLE IF EXISTS ");
        builder.append(UsageStatEntry.TABLE_NAME).append(";\n");
        SQL_DELETE_ENTRIES = builder.toString();
    }

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static SQLiteHelper getInstance(Context context) {
        if (_instance == null) {
            _instance = new SQLiteHelper(context);
        }
        return _instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_BASE_LINE);
        sqLiteDatabase.execSQL(CREATE_TABLE_USAGE_STAT);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, String.format("About to create DB version %s", newVersion));
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.d(LOG_TAG, String.format(" SQL statement for deletion: %s", SQL_DELETE_ENTRIES));
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static String copyDatabaseToExternalStorage(Context context, String pFilename) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        InputStream in = null;
        OutputStream os = null;
        String fullFileName = null;
        String dirForDatabase = null;
        try {
            in = new FileInputStream("data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME);

            File externalFilesDir = context.getExternalFilesDir(null);
            dirForDatabase = externalFilesDir.getAbsolutePath() + "/simpleplus/databases";
            File dir = new File(dirForDatabase);
            if (dir.exists() || dir.mkdirs()) {
                String fileName = pFilename + sdf.format(new Date()) + ".sqlite";

                File file = new File(dir, fileName);
                os = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                fullFileName = file.getAbsolutePath();
            } else {
                RemoteLogger.e(LOG_TAG, String.format("No se pudo crear/acceder el directorio %s", dirForDatabase));
            }
        } catch (FileNotFoundException e) {
            RemoteLogger.e(LOG_TAG, "Archivo para la base de datos no encontrado", e);
        } catch (IOException e) {
            RemoteLogger.e(LOG_TAG, "Error I/O al copiar la base de datos", e);
        } finally {
            closeStream(in);
            closeStream(os);
        }
        return fullFileName;
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

    public void closeDb(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }
}
