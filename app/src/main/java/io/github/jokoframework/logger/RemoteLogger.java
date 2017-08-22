package io.github.jokoframework.logger;


import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.jokoframework.aplicationconstants.Constants;
import io.github.jokoframework.pojo.RemoteLogPojo;

public class RemoteLogger {

        private static final int MAX_LOG_COUNT_TO_FLUSH = 3; // se cambio para poder probar y tener una vista, anterior era 20...
        private static final String LOG_TAG = RemoteLogger.class.getSimpleName();

        private static List<RemoteLogPojo> logPojos = new ArrayList<>();

        private static Date latestUpdate = new Date();

        private RemoteLogger() {
        }

        public static int v(String tag, String msg) {
            addLog("VERBOSE", tag, msg, null);
            return Log.v(tag, msg);
        }

        public static int v(String tag, String msg, Throwable tr) {
            addLog("VERBOSE", tag, msg, tr);
            return Log.v(tag, msg, tr);
        }

        public static int d(String tag, String msg) {
            addLog("DEBUG", tag, msg, null);
            return Log.d(tag, msg);
        }

        private static void addLog(String level, String tag, String msg, Throwable throwable) {
            RemoteLogPojo logPojo = new RemoteLogPojo(level, tag, msg, getStackTraceString(throwable));
            logPojos.add(logPojo);
            //Flusheamos al parse cada x líneas o cada 1 minuto
            if (logPojos.size() >= MAX_LOG_COUNT_TO_FLUSH || (System.currentTimeMillis() - latestUpdate.getTime() > Constants.ONE_MINUTE)) {
                Log.d(LOG_TAG, "Flushing logs to parse");
                new AsyncRemoteLogToParse().execute(logPojos.toArray(new RemoteLogPojo[logPojos.size()]));
                logPojos = new ArrayList<>();
                latestUpdate = new Date();
            } else {
                Log.d(LOG_TAG, String.format("Cantidad de remotelogs: %s", logPojos.size()));
            }
        }

        public static int d(String tag, String msg, Throwable tr) {
            addLog("DEBUG", tag, msg, tr);
            return Log.d(tag, msg, tr);
        }

        public static int i(String tag, String msg) {
            addLog("INFO", tag, msg, null);
            return Log.i(tag, msg);
        }

        public static int i(String tag, String msg, Throwable tr) {
            addLog("INFO", tag, msg, tr);
            return Log.i(tag, msg, tr);
        }

        public static int w(String tag, String msg) {
            addLog("WARN", tag, msg, null);
            return Log.w(tag, msg);
        }

        public static int w(String tag, String msg, Throwable tr) {
            addLog("WARN", tag, msg, tr);
            return Log.w(tag, msg, tr);
        }

        public static int w(String tag, Throwable tr) {
            addLog("WARN", tag, null, tr);
            return Log.w(tag, tr);
        }

        public static int e(String tag, String msg) {
            addLog("ERROR", tag, msg, null);
            return Log.e(tag, msg);
        }

        public static int e(String tag, String msg, Throwable tr) {
            addLog("ERROR", tag, msg, tr);
            return Log.e(tag, msg, tr);
        }

        public static int wtf(String tag, String msg) {
            addLog("WTF", tag, msg, null);
            return Log.wtf(tag, msg);
        }

        public static int wtf(String tag, Throwable tr) {
            addLog("WTF", tag, null, tr);
            return Log.wtf(tag, tr);
        }

        public static int wtf(String tag, String msg, Throwable tr) {
            addLog("WTF", tag, msg, tr);
            return Log.wtf(tag, msg, tr);
        }

        public static String getStackTraceString(Throwable tr) {
            return Log.getStackTraceString(tr);
        }

        public static int println(int priority, String tag, String msg) {
            return Log.println(priority, tag, msg);
        }
}
