package io.github.jokoframework;

import android.os.AsyncTask;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class AsyncRemoteLogToParse extends AsyncTask<RemoteLogPojo,Integer,Long> {

    private static final String LOG_TAG = AsyncRemoteLogToParse.class.getSimpleName();
    //Sólo en etapa de desarrollo habilitamos el log remoto al Parse para diagnósticos
    private static final boolean DEVELOP_MODE = true;

    @Override
    protected Long doInBackground(RemoteLogPojo... remoteLogPojos) {
        final List<ParseObject> logsToParse = new ArrayList<ParseObject>();
//        final List<Map<String,String>> logsToParse = new ArrayList<Map<String,String>>();

        RemoteLogCapable remoteLogCapable = new ParseLogger();
        for (RemoteLogPojo remoteLogPojo : remoteLogPojos) {
            //BEGIN-IGNORE-SONARQUBE
            ParseObject log = new ParseObject(Constants.PARSE_CLASS_REMOTE_LOG);
//            Map<String,String> log = new HashMap<>();
            if (remoteLogPojo.getMessage() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_MESSAGE, remoteLogPojo.getMessage());
            }
            if (remoteLogPojo.getStackTrace() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_STACKTRACE, remoteLogPojo.getStackTrace());
            }
            if (remoteLogPojo.getLogTag() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_LOG_TAG, remoteLogPojo.getLogTag());
            }
            if (remoteLogPojo.getLevel() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_LEVEL, remoteLogPojo.getLevel());
            }
            if (ParseUser.getCurrentUser() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_USERNAME, ParseUser.getCurrentUser().getUsername());
            }
            if (remoteLogPojo.getSavedAt() != null) {
                log.put(Constants.PARSE_ATTRIBUTE_SAVED_AT, String.valueOf(remoteLogPojo.getSavedAt()));
            }

            if (StringUtils.isBlank(remoteLogPojo.getAppVersion())) {
                log.put(Constants.PARSE_ATTRIBUTE_APP_VERSION,
                        String.format("version: %s", RemoteLogPojo.BUILD_VERSION));
            }
            //END-IGNORE-SONARQUBE
            logsToParse.add(log);
        }
        if (!logsToParse.isEmpty() && DEVELOP_MODE) {
            //remoteLogCapable.flushLogs(logsToParse);
        }
        return (long) logsToParse.size();
    }
}

