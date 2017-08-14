package io.github.jokoframework;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by joaquin on 11/08/17.
 */

public class LoggerCapable implements RemoteLogCapable {

    private static final String LOG_TAG = AsyncRemoteLogToParse.class.getSimpleName();

    @Override
    public void flushLogs(final List<ParseObject> logsToParse) {
        ParseObject.saveAllInBackground(logsToParse, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(LOG_TAG, String.format("Logs guardados correctamente. Total : %s", logsToParse.size()));
                } else {
                    Log.e(LOG_TAG, "Error guardando los logs al parse", e);
                }
            }
        });
    }

//    @Override
//    public void flushLogs(List<Map<String,String>> logsToParse) {
//        ParseObject.saveAllInBackground(logsToParse, new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d(LOG_TAG, String.format("Logs guardados correctamente. Total : %s", logsToParse.size()));
//                } else {
//                    Log.e(LOG_TAG, "Error guardando los logs al parse", e);
//                }
//            }
//        });
//    }
}
