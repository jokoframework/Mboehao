package io.github.jokoframework.aplicationconstants;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

/**
 * Created by joaquin on 10/08/17.
 */

public class Constants {

    public static final double DATABASE_VERSION = 1.0;
    public static final String DATABASE_NAME = String.format("mboehao-v%s.db", DATABASE_VERSION);
    public static final int VERSION_CODE = 33;

    /**
     * Constantes de tiempos comúnmente utilizadas, en milisegundos
     */
    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = ONE_SECOND * 60L;
    public static final long ONE_HOUR = ONE_MINUTE * 60L;
    public static final long ONE_DAY = ONE_HOUR * 24L;

    /**
     * Por default todos los índices de las cápsulas empiezan en 0
     * hay siete tipos de cápsulas
     */

    public static final int DAYS_TO_SHOW_IN_GRAPH_HISTORY = 14;

    public static final int MIN_DAILY_TEST_IMAGE_MIN_HEIGHT = 95;
    public static final int MIN_WARNING_POINTS_SIZE = 16;
    public static final long LOCAL_DATA_MAX_AGE_IN_MILLIS = Constants.DAYS_TO_SHOW_IN_GRAPH_HISTORY * Constants.ONE_DAY;


    /**
     * Si tiene este valor, quiere decir que no hay al menos 4 muestras de dailyTests disponibles
     * para calcular el promedio. Cambiamos a este valor, debido a una incompatibilidad con el cliente
     * iOS que no soportaba el Double.MIN_VALUE
     */
    public static final double HD_VERTICAL_RESOLUTION = 1280;
    public static final double TABLET_VERTICAL_RESOLUTION = 1920;
    public static final double HVGA_VERTICAL_RESOLUTION = 480;


    //Miscelaneos
    public static final Integer RISK = 1;
    public static final Integer LIFE_EVENT = 2;
    public static final Integer MEDICATION = 3;
    public static final Integer PRODROM_SYMTOMS = 4;
    public static final Integer MIXED_SYMPTOMS = 5;
    public static final Integer SUBSTANCE = 6;
    public static final String DATE_TIME_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";


    //Defaults
    public static final int DEFAULT_WARNING_POINTS_SIZE = 32;


    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    public static final String USER_PREFS_USER = "U";
    public static final String USER_PREFS_PW = "P";

    //Extras
    public static final String EXTRA_DRUG_DESCRIPTION = "drugDescription";

    public static final String EXTRA_TIME_TABLE_DESCRIPTION = "timeTableDescription";


    //Referentes al parse
    public static final String PARSE_PARAMETER = "Parameter";
    public static final String PARSE_PARAMETER_DESCRIPTION = "description";
    public static final String PARSE_PARAMETER_VALUE = "value";

    public static final String PARSE_CLASS_REMOTE_LOG = "RemoteLog";

    public static final String PARSE_ATTRIBUTE_LEVEL = "level";
    public static final String IMAGE_PNG = "image/png";
    public static final String IMAGE_JPG = "image/jpg";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String IMAGE_SELECTION = "image/*";
    public static final String PARSE_ATTRIBUTE_LOG_TAG = "logTag";
    public static final String PARSE_ATTRIBUTE_MESSAGE = "message";
    public static final String PARSE_ATTRIBUTE_OBJECT_ID = "objectId";
    public static final String PARSE_ATTRIBUTE_SAVED_AT = "savedAt";
    public static final String PARSE_ATTRIBUTE_STACKTRACE = "stackTrace";
    public static final String PARSE_ATTRIBUTE_APP_VERSION = "appVersion";
    public static final String PARSE_ATTRIBUTE_USERNAME = "username";



    private static final List<String> WEB_VIEW_URLS_WITHOUT_PROGRESS_BAR = Arrays.asList("track.php",
            "secure.gravatar.com", "googlesyndication.com", "skimresources.com",
            "scorecardresearch.com", "www.google.com/ads", "doubleckick.net",
            "afyn11.net", "googleadservices.com", "googletagservices.com",
            "adroll.com", "advertising", "mookiel.com", "maps.googleapis.com",
            "developers.google.com");
    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final String HTML_SCALED_FONT_SIZE = "3";
    public static final String EXTENDED_FILE = "extended";
    public static final String SHARED_MBOEHAO_PREF = "MboehaoPref";


    private Constants() {
         /*Porque los constructores no tienen que tener instancias...
         Y menos esta clase de constantes...*/
    }

    public static final String GRAPH_DATA_LAST_MODIFIED = "graphLastModified";
    public static final String GRAPH_DATE_CREATED = "graphDateCreated";

}
