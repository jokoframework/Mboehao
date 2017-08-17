package io.github.jokoframework.aplicationconstants;

/**
 * Created by joaquin on 10/08/17.
 */

public class Constants {

    public static final String PARSE_DATA_SAVE_ENTRY = "Entry Data";
    public static final String PARSE_ATTRIBUTE_DESCRIPTION ="Description" ;
    public static final String PARSE_ATTRIBUTE_USERNAME = "Username";
    public static final String USER_PREFS_USER = "U";
    public static final String USER_PREFS_PW = "P";
    public static final String DATE_TIME_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final int VERSION_CODE = 33;

    public static final String PARSE_ATTRIBUTE_MESSAGE = "message";
    public static final String PARSE_ATTRIBUTE_STACKTRACE = "stackTrace";
    public static final String PARSE_ATTRIBUTE_LOG_TAG = "logTag";
    public static final String PARSE_ATTRIBUTE_LEVEL = "level";
    public static final String PARSE_ATTRIBUTE_SAVED_AT = "savedAt";
    public static final String PARSE_ATTRIBUTE_APP_VERSION = "appVersion";
    public static final String PARSE_CLASS_REMOTE_LOG = "RemoteLog";


    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = ONE_SECOND * 60L;
    public static final long ONE_HOUR = ONE_MINUTE * 60L;
    public static final long ONE_DAY = ONE_HOUR * 24L;

    private Constants() {
        // Porque los constructores no tienen que tener instanciados...
        // Y menos esta clase de constantes...
    }
}
