package io.github.jokoframework.logger;

import android.os.AsyncTask;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.pojo.RemoteLogPojo;
import io.github.jokoframework.utilitys.ParseUtils;


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

            final ParseACL defaultAcl = ParseUtils.getDefaultAcl(ParseUser.getCurrentUser());
            convertoParseObject(log).setACL(defaultAcl); //log no es un ParseObject...

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
            remoteLogCapable.flushLogs(logsToParse); // guarda los log, si tiene alguno, y ademas esta en modo de Desarrollo...
        }
        return (long) logsToParse.size();
    }

    private ParseObject convertoParseObject(Map<String,Object> log){
        ParseObject currentParseObject = new ParseObject(Constants.PARSE_CLASS_REMOTE_LOG);
        Iterator<String> keyIterator = log.keySet().iterator(); // iterador del Map...
        while (keyIterator.hasNext()) {
            String currentKey = keyIterator.next();
            Object currentValue = log.get(currentKey);
            currentParseObject.put(currentKey, currentValue);
        }
        return currentParseObject;
    }
}

