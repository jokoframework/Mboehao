package io.github.jokoframework.logger;

import android.os.AsyncTask;

import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.pojo.RemoteLogPojo;


public class AsyncRemoteLogToParse extends AsyncTask<RemoteLogPojo,Integer,Long> {

    static final String LOG_TAG = AsyncRemoteLogToParse.class.getSimpleName();
    //Sólo en etapa de desarrollo habilitamos el log remoto al Parse para diagnósticos
    private static final boolean DEVELOP_MODE = true;

    @Override
    protected Long doInBackground(RemoteLogPojo... remoteLogPojos) {
        final List<Map<String,Object>> logsToParse = new ArrayList<>();

        RemoteLogCapable remoteLogCapable = new ParseLogger();
        for (RemoteLogPojo remoteLogPojo : remoteLogPojos) {
            //BEGIN-IGNORE-SONARQUBE
            Map<String,Object> log = new HashMap<>();
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
            remoteLogCapable.flushLogs(logsToParse);
        }
        return (long) logsToParse.size();
    }
}

