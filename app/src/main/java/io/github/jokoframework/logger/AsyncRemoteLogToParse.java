package io.github.jokoframework.logger;

import android.os.AsyncTask;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.jokoframework.constants.AppConstants;
import io.github.jokoframework.pojo.RemoteLogPojo;


public class AsyncRemoteLogToParse extends AsyncTask<RemoteLogPojo, Integer, Long> {

    //Sólo en etapa de desarrollo habilitamos el log remoto al Parse para diagnósticos
    private static final boolean DEVELOP_MODE = true;

    @Override
    protected Long doInBackground(RemoteLogPojo... remoteLogPojos) {
        final List<Map<String, Object>> logsToParse = new ArrayList<>();

        RemoteLogCapable remoteLogCapable = null;
        for (RemoteLogPojo remoteLogPojo : remoteLogPojos) {
            //BEGIN-IGNORE-SONARQUBE
            Map<String, Object> log = new HashMap<>();

            setConditionalMessage(remoteLogPojo, log);
            setConditionalUserAndDate(remoteLogPojo, log);

            if (StringUtils.isBlank(remoteLogPojo.getAppVersion())) {
                log.put(AppConstants.PARSE_ATTRIBUTE_APP_VERSION,
                        String.format("version: %s", RemoteLogPojo.BUILD_VERSION));
            }
            //END-IGNORE-SONARQUBE
            logsToParse.add(log);
        }
        if (!logsToParse.isEmpty() && DEVELOP_MODE) {
            remoteLogCapable.flushLogs(logsToParse); // guarda los log, si tiene alguno, y ademas esta en modo de Desarrollo...
        }
        return (long) logsToParse.size();
    }

    private void setConditionalUserAndDate(RemoteLogPojo remoteLogPojo, Map<String, Object> log) {
        if (remoteLogPojo.getLevel() != null) {
            log.put(AppConstants.PARSE_ATTRIBUTE_LEVEL, remoteLogPojo.getLevel());
        }
        if (remoteLogPojo.getSavedAt() != null) {
            log.put(AppConstants.PARSE_ATTRIBUTE_SAVED_AT, String.valueOf(remoteLogPojo.getSavedAt()));
        }
    }

    private void setConditionalMessage(RemoteLogPojo remoteLogPojo, Map<String, Object> log) {
        if (remoteLogPojo.getMessage() != null) {
            log.put(AppConstants.PARSE_ATTRIBUTE_MESSAGE, remoteLogPojo.getMessage());
        }
        if (remoteLogPojo.getStackTrace() != null) {
            log.put(AppConstants.PARSE_ATTRIBUTE_STACKTRACE, remoteLogPojo.getStackTrace());
        }
        if (remoteLogPojo.getLogTag() != null) {
            log.put(AppConstants.PARSE_ATTRIBUTE_LOG_TAG, remoteLogPojo.getLogTag());
        }
    }

}

