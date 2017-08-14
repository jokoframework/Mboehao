package io.github.jokoframework;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by joaquin on 11/08/17.
 */

public class ParseLogger implements RemoteLogCapable {

    private static final String LOG_TAG = AsyncRemoteLogToParse.class.getSimpleName();

    private void flushLogsToParseServer(final List<ParseObject> logsToParse) {
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

    @Override
    public void flushLogs(final List<Map<String, Object>> logsToParse) {
        flushLogsToParseServer(convertToParseObject(logsToParse));
    }

    private List<ParseObject> convertToParseObject(List<Map<String, Object>> logsToParse) {
        List<ParseObject> parseObjects = new ArrayList<>();
        Iterator<Map<String, Object>> iteratorLogs = logsToParse.iterator();
        while (iteratorLogs.hasNext()) {
            Map<String, Object> currentLog = iteratorLogs.next();
            ParseObject currentParseObject = new ParseObject("AAAA");
            Iterator<String> keyIterator = currentLog.keySet().iterator();
            while (keyIterator.hasNext()) {
                String currentKey = keyIterator.next();
                Object currentValue = currentLog.get(currentKey);
                currentParseObject.put(currentKey, currentValue);
                parseObjects.add(currentParseObject);
            }
        }
        return parseObjects;
    }
}
