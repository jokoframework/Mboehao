package io.github.jokoframework.aplicationconstants;

import android.graphics.Color;

import java.util.Arrays;
import java.util.List;

/**
 * Created by joaquin on 10/08/17.
 */

public class Constants {
    public static final int VERSION_CODE = 33;

    /**
     * Constantes de tiempos comúnmente utilizadas, en milisegundos
     */
    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = ONE_SECOND * 60L;
    public static final long ONE_HOUR = ONE_MINUTE * 60L;
    public static final long ONE_DAY = ONE_HOUR * 24L;

    public static final float EPSILON = 0.0000001f;

    public static final int NUMBER_OF_WEEKS_PER_YEAR = 53;

    public static final int YES = 1;
    public static final int NO = 0;
    /**
     * El identificador numérico de las preguntas
     */
    public static final int P1 = 0;
    public static final int P1_SUBQUESTIONS_COUNT = 7;
    public static final int P2 = 1;
    public static final int P3 = 2;
    public static final int P3_SUBQUESTIONS_COUNT = 6;
    public static final int P4 = 3;
    public static final int P5 = 4;
    public static final int P6 = 6;
    public static final int P7 = 7;
    public static final int P8 = 8;
    /**
     * Esto tiene que ver con el orden que tiene en el questions.xml
     */
    public static final int P9 = 5;
    public static final int NO_SUBQUESTION = 0;
    //Si entra desde el EndQuestions se le pasa como extra el P6 como primera pregunta
    public static final int MAX_QUESTIONS_FROM_END_QUESTIONS = 3;

    public static final int MAX_QUESTIONS_INDEX = 8;
    public static final int MAX_QUESTIONS_INDEX_WITH_PRODROMICS = 9;
    /**
     * Por default todos los índices de las cápsulas empiezan en 0
     * hay siete tipos de cápsulas
     */
    public static final int CAPSULE_GENERALES = 0;
    public static final int CAPSULE_RECAIDAS = 1;
    public static final int CAPSULE_ADHERENCIA_MEDICACION = 2;
    public static final int CAPSULE_CONSUMO_TOXICOS = 3;
    public static final int CAPSULE_SUENHO = 4;
    public static final int CAPSULE_EPISODIO_MANIACO = 5;
    public static final int CAPSULE_EPISODIO_DEPRESIVO = 6;
    public static final int NOTIFICATION_CAPSULE_ID = 22;
    public static final int NOTIFICATION_DAILY_TEST_ID = 33;
    public static final int INCLUDE_CATEGORY = 1;
    public static final int P1_2_INDEX = 1;
    public static final int P3_2_INDEX = 1;
    public static final int EXCLUDE_CATEGORY = 0;
    public static final int DAYS_TO_SHOW_IN_GRAPH_HISTORY = 14;
    public static final int MAX_DAY_FOR_PREVIOUS_WEEK_AVERAGE = DAYS_TO_SHOW_IN_GRAPH_HISTORY;
    public static final int MAX_DAYS_FOR_LIFE_EVENTS = DAYS_TO_SHOW_IN_GRAPH_HISTORY;
    public static final int MAX_DAYS_FOR_LONG_RANGE_PLOT = 28;
    public static final int MIN_DAY_FOR_PREVIOUS_WEEK_AVERAGE = 4;
    public static final int MIN_DAILY_TEST_IMAGE_MIN_HEIGHT = 95;
    public static final int MIN_WARNING_POINTS_SIZE = 16;
    public static final int MINIMUM_WIDTH_RESOLUTION_FOR_SIDE_MENU_LOGO = 321;
    public static final long LOCAL_DATA_MAX_AGE_IN_MILLIS = Constants.DAYS_TO_SHOW_IN_GRAPH_HISTORY * Constants.ONE_DAY;
    public static final Float CURRENT_DAY_X_COORDINATE = 13f;

    /**
     * Si tiene este valor, quiere decir que no hay al menos 4 muestras de dailyTests disponibles
     * para calcular el promedio. Cambiamos a este valor, debido a una incompatibilidad con el cliente
     * iOS que no soportaba el Double.MIN_VALUE
     */
    public static final double MIN_VALUE_WEEK_AVERAGE_THRESHOLD = 1e-4;
    public static final double DAILY_TEST_MAX_VALUE = 40.0;
    public static final double DAILY_TEST_MIN_VALUE = -40.0;
    public static final double HD_VERTICAL_RESOLUTION = 1280;
    public static final double TABLET_VERTICAL_RESOLUTION = 1920;
    public static final double HVGA_VERTICAL_RESOLUTION = 480;
    public static final int MINIMUM_HEIGHT_FOR_FULL_PLOT_SIZE = 481;
    public static final int COLOR_VERY_LOW = Color.parseColor("#397aa3");
    public static final int COLOR_LOW = Color.parseColor("#52c5d0");
    public static final int COLOR_NORMAL = Color.parseColor("#84cca5");
    public static final int COLOR_HIGH = Color.parseColor("#f2c05f");
    public static final int COLOR_VERY_HIGH = Color.parseColor("#f2685f");
    public static final int COLOR_PLOT = Color.parseColor("#c2ecdd");
    public static final int COLOR_PLOT_NORMAL_DOT = Color.parseColor("#9c9c9c");
    public static final int COLOR_PLOT_EVENT_DOT = Color.parseColor("#69D49A");
    public static final int COLOR_PLOT_LABEL = Color.parseColor("#9c9c9c");
    public static final int COLOR_PLOT_NO_DAILY_TEST_RESULT = Color.parseColor("#f2c05f");

    public static final String CALLBACK_EVENT_DRAW_GRAPH = "drawGraph";
    public static final int PLOT_TEXT_SIZE = 14;
    public static final int PLOT_MARGIN = 15;
    public static final int PLOT_RANGE_STEP = 10;
    public static final int DOMAIN_LEFT_MIN = 0;
    public static final int RANGE_TOP_MIN = 40;
    public static final int RANGE_TOP_MAX = 40;
    public static final int RANGE_BOTTOM_MAX = -40;
    public static final int RANGE_BOTTOM_MIN = -40;
    public static final String PLOT_LABEL_MUY_BAJO = "MUY BAJO";
    public static final String PLOT_LABEL_BAJO = "BAJO";
    public static final String PLOT_LABEL_ELEVADO = "ELEVADO";
    public static final String PLOT_LABEL_MUY_ELEVADO = "MUY ELEVADO";
    public static final String PLOT_LABEL_NORMAL = "NORMAL";
    public static final int PLOT_MARKER_TEXT_SIZE = 12;
    public static final int PLOT_VERY_LOW_Y = -20;
    public static final int PLOT_LOW_Y = -10;
    public static final int PLOT_HIGH_Y = 10;
    public static final int PLOT_VERY_HIGH_Y = 20;
    public static final int PLOT_NORMAL_Y = 1;

    //Miscelaneos
    public static final Integer RISK = 1;
    public static final Integer LIFE_EVENT = 2;
    public static final Integer MEDICATION = 3;
    public static final Integer PRODROM_SYMTOMS = 4;
    public static final Integer MIXED_SYMPTOMS = 5;
    public static final Integer SUBSTANCE = 6;
    public static final int NUMBER_OF_TIME_TABLES = 4;
    public static final int QUESTION_NUMBER_MEDICATION = 5;
    public static final int QUESTION_NUMBER_ENERGY = 2;
    public static final int TIMES_FOR_DICE_ROLLING = 3;
    public static final int QUESTION_SUICIDAL = 5;
    public static final int DAILY_TEST_DAYS_NOT_DONE_WARNING = 5;
    public static final String FONT_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String FONT_BOLD = "fonts/Montserrat-Bold.ttf";
    public static final String NS = null;
    public static final String CALLBACK_STATUS_OK = "ok";
    public static final String CALLBACK_EVENT_DRAW_WEEK_GRAPH = "drawWeekGraph";
    public static final String CSV_SEPARATOR = ";";
    public static final String DATE_TIME_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEPRESSIVE = "DEPRESIVA";
    public final static String EMAIL_HELP = "dahidalg@clinic.ub.es";
    public static final String GRAPH_DATA_LAST_MODIFIED = "graphLastModified";
    public static final String GRAPH_DATE_CREATED = "graphDateCreated";
    public static final String LOCALE_ES = "ES";
    public static final String IMAGE_PNG = "image/png";
    public static final String MANIAC = "MANIACA";
    public static final String MARKET_SIMPLE_URL = "market://details?id=py.com.sodep.simpleplus";
    public static final String MEDS_BY_TIME_TABLE_PREFIX = "medsByTimeTable_";
    public static final String MIXED = "MIXTA";
    public static final String MULTI_VALUES_SEPARATOR = "_";
    public static final String TEST_TYPE_DAILY = "daily";
    public static final String TEST_TYPE_WEEKLY = "weekly";
    public static final String TIME_PICKER_ARGUMENT_HOUR = "hour";
    public static final String TIME_PICKER_ARGUMENT_MINUTE = "minute";
    public static final String TIME_PICKER_ARGUMENT_TITLE = "title";
    public static final String STRING_YES = "1";
    public static final String EXTENDED_FILE = "extended";
    public static final String DATE_PICKER = "datePicker";

    //Defaults
    public static final String DEFAULT_AD_TIME = "23:00";
    public static final String DEFAULT_ALARM_TIME = "10:00";
    public static final String DEFAULT_BREAKFAST_TIME = "07:00";
    public static final String DEFAULT_CAPSULE = "0";
    public static final String DEFAULT_CAPSULE_INDEX_SHOWED = "0_0_0_0_0_0_0";
    public static final String DEFAULT_CAPSULES_TO_SHOW = "0_0_0_0_0_0_0";
    public static final String DEFAULT_CAPSULE_HOUR = "12:00";
    public static final String DEFAULT_DINNER_TIME = "20:00";
    public static final String DEFAULT_LUNCH_TIME = "13:00";
    public static final double DEFAULT_DAILY_SCORE = 1.0;
    public static final int DEFAULT_MAX_QUESTIONS_COUNT = 0;
    public static final int DEFAULT_MEDICATION_SCORE = 3;
    public static final int DEFAULT_NO_ACTION_ANSWER = -1;
    public static final int DEFAULT_WARNING_POINTS_SIZE = 32;

    //XML parsers capsula
    public static final String EVENT_NAME_CAPS_XML = "capsXml";
    public static final String FILE_CAPSULES = "capsules";
    public static final String XML_TAG_CAPSULAS = "capsulas";
    public static final String XML_TAG_TIPO = "tipo";
    public static final String XML_ATTRIBUTE_INDEX = "index";
    public static final String XML_TAG_CAPSULA = "capsula";
    //XML parsers test
    public static final String FILE_QUESTIONS = "questions";
    public static final String FILE_QUESTIONS_XML = "questions.xml";
    public static final String EVENT_NAME_TEST_XML = "testXml";
    public static final String XML_TAG_TEST = "test";
    public static final String XML_TAG_QUESTION = "question";
    public static final String XML_TAG_CONTENT = "content";
    public static final String XML_TAG_SUBQUESTION = "subquestion";
    public static final String XML_TAG_TITLE_SQ = "titleSQ";

    //Referente al Recipe
    public static final int POSICION_DESAYUNO = 0;
    public static final int POSICION_ALMUERZO = 1;
    public static final int POSICION_CENA = 2;
    public static final int POSICION_ANTES_DORMIR = 3;
    public static final String ANTES_DORMIR = "AD";
    public static final String CENA = "Cena";
    public static final String DESAYUNO = "Desayuno";
    public static final String ALMUERZO = "Almuerzo";
    public static final int DO_TAKE = 1;

    //Gamification
    public static final double WOOD_MIN_POINTS = 40.0;
    public static final double WOOD_MAX_POINTS = 50.0;
    public static final double BRONZE_MIN_POINTS = 51.0;
    public static final double BRONZE_MAX_POINTS = 70.0;
    public static final double SILVER_MIN_POINTS = 71.0;
    public static final double SILVER_MAX_POINTS = 90.0;
    public static final double GOLD_MIN_POINTS = 91.0;
    public static final int MAX_MEDALS_PER_PAGE = 9;
    public static final int MAX_MEDALS_HISTORY = 18;
    public static final int MAX_POINTS_PER_WEEK = 26;
    public static final int SCORE_FOR_VIEW_CAPSULE = 1;
    public static final int SCORE_FOR_DAILY_TEST = 2;
    public static final int SCORE_FOR_WEEKLY_TEST = 5;
    public static final int SCORE_FOR_REMINDER = 1;
    public static final int SCORE_FOR_LIFE_EVENT = 1;
    public static final String WOOD = "Wood";
    public static final String BRONZE = "Bronze";
    public static final String SILVER = "Silver";
    public static final String GOLD = "Gold";
    public static final String SCORE_ORIGIN_FROM_CAPSULE = "Capsule";
    public static final String SCORE_ORIGIN_FROM_DAILY_TEST = "DailyTest";
    public static final String SCORE_ORIGIN_FROM_WEEKLY_TEST = "WeeklyTest";
    public static final String SCORE_ORIGIN_FROM_REMINDER = "Reminder";
    public static final String SCORE_ORIGIN_FROM_LIFE_EVENT = "LifeEvent";


    //Preferencias
    public static final String SHARED_SIMPLE_PREF = "SimplePref";

    public static final String PREFERENCE_CAPSULES_INDEX_SHOWED_BY_CATEGORY = "capsulesIndexShowedByCategory";
    public static final String PREFERENCE_CAPSULES_TYPES_TO_SHOW = "capsulesTypesToShow";
    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    public static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    public static final String USER_PREFS_USER = "U";
    public static final String USER_PREFS_PW = "P";
    public static final String USER_PREFS_HELP_SHOW_COUNT = "USER_PREFS_HELP_SHOW_COUNT";

    public static final String USER_PREFERENCE_CAPSULE_INDEX_GENERALES = "capsuleIndexGenerales";
    public static final String USER_PREFERENCE_CAP_INDEX_RECAIDAS = "capIndexRecaidas";
    public static final String USER_PREFERENCE_CAP_INDEX_ADHERENCIA_MEDICACION = "capIndexAdherenciaMedicacion";
    public static final String USER_PREFERENCE_CAP_INDEX_CONSUMO_TOXICOS = "capIndexConsumoToxicos";
    public static final String USER_PREFERENCE_CAP_INDEX_SUENHO = "capIndexSuenho";
    public static final String USER_PREFERENCE_CAP_INDEX_EPISODIO_MANIACO = "capIndexEpisodioManiaco";
    public static final String USER_PREFERENCE_CAP_INDEX_EPISODIO_DEPRESIVO = "capIndexEpisodioDepresivo";
    public static final String USER_PREFERENCE_CAPSULE = "capsule";
    public static final String USER_PREFERENCE_DRUG_POJOS = "drugPojos";
    public static final String USER_PREFERENCE_FIRST_PRODROMIC_SHOW = "fist_entry_prodromic";
    public static final String USER_PREFERENCE_GREETED_USER = "GREETED_USER";
    public static final String USER_PREFERENCE_LATEST_UPDATE = "recipeLatestUpdate";
    public static final String USER_PREFERENCE_LATEST_RECIPE_UPDATE_FROM_PARSE = "latestRecipeUpdateFromParse";
    public static final String USER_PREFERENCE_MEDICATION_POJOS = "medicationPojos";
    public static final String USER_PREFERENCE_NOTIFICATION_IDS = "notificationIds";
    public static final String USER_PREFERENCE_SINCRONIZED_WITH_PARSE = "sincronizedWithParse";
    public static final String USER_PREFERENCE_TIME_TABLE_POJOS = "timeTablePojos";
    public static final String USER_PREFERENCE_UPDATE_PARSE_DATA_ON_RESUME = "updateParseDataOnResume";

    //Extras
    public static final String EXTRA_AD = "ad";
    public static final String EXTRA_BREAKFAST = "breakfast";
    public static final String EXTRA_DINNER = "dinner";
    public static final String EXTRA_DRUG_DESCRIPTION = "drugDescription";
    public static final String EXTRA_EDIT_RECIPE = "editarReceta";
    public static final String EXTRA_ERROR_TEXT = "error_text";
    public static final String EXTRA_FROM_END_QUESTION_ACTIVITY = "fromEndQuestionActivity";
    public static final String EXTRA_FROM_WEEKLY = "weekly";
    public static final String EXTRA_HELP_ID = "help_id";
    public static final String EXTRA_LUNCH = "lunch";
    public static final String EXTRA_NOTIFICATION_ID = "notificationId";
    public static final String EXTRA_ORIGIN = "origin";
    public static final String EXTRA_PRODROMIC_TUTORIAL = "prodromicTutorialExtra";
    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_RELOAD_PARAMETERS = "reloadParametersDrugAndTimeTables";
    public static final String EXTRA_SNOOZED_ALARM = "snoozedAlarm";
    public static final String EXTRA_SUBQUESTION = "subquestion";
    public static final String EXTRA_TIME_TABLE_DESCRIPTION = "timeTableDescription";
    public static final String EXTRA_UPDATE_PARSE_DATA = "UPDATE_PARSE_DATA";
    public static final String EXTRA_VIBRATE = "vibrate";

    //Referentes al parse
    public static final String PARSE_PARAMETER = "Parameter";
    public static final String PARSE_PARAMETER_DESCRIPTION = "description";
    public static final String PARSE_PARAMETER_VALUE = "value";
    public static final Object PARSE_PARAMETER_URL_TUTORIAL = "urlTutorial";
    public static final String PARSE_PARAMETER_URL_PRODROMIC = "urlProdromicAndroid";

    public static final String PARSE_CLOUD_CODE_FUNCTION_SEND_THERAPIST = "sendTherapist";

    public static final String PARSE_CLASS_CALIFICATION = "Calification";
    public static final String PARSE_CLASS_DAILY_TEST = "DailyTest";
    public static final String PARSE_CLASS_DRUG = "Drug";
    public static final String PARSE_CLASS_LIFE_EVENT = "LifeEvent";
    public static final String PARSE_CLASS_MEDICATION = "Medication";
    public static final String PARSE_CLASS_PRODROMIC_QUESTION = "ProdromicQuestion";
    public static final String PARSE_CLASS_RECIPE_ACTIVITY = "RecipeActivity";
    public static final String PARSE_CLASS_REMOTE_LOG = "RemoteLog";
    public static final String PARSE_CLASS_TIME_TABLE = "TimeTable";
    public static final String PARSE_CLASS_TROPHY = "Trophy";
    public static final String PARSE_CLASS_WEEK_TEST = "WeekTest";
    public static final String PARSE_CLASS_CAPSULE = "Capsule";

    public static final String PARSE_ATTRIBUTE_ACTIVE = "active";
    public static final String PARSE_ATTRIBUTE_AD_TIME = "adTime";
    public static final String PARSE_ATTRIBUTE_ANSWERS = "answers";
    public static final String PARSE_ATTRIBUTE_BITMAP_IDS = "bitmapIds";
    public static final String PARSE_ATTRIBUTE_BREAKFAST_TIME = "breakfastTime";
    public static final String PARSE_ATTRIBUTE_CALIFICATION_SCORE = "score";
    public static final String PARSE_ATTRIBUTE_CAPSULE = "capsule";
    public static final String PARSE_ATTRIBUTE_CAPSULE_HOUR = "capsuleHour";
    public static final String PARSE_ATTRIBUTE_CREATED_AT = "createdAt";
    public static final String PARSE_ATTRIBUTE_DATE_AS_STRING = "dateAsString";
    public static final String PARSE_ATTRIBUTE_DEFAULT_TIME = "defaultTime";
    public static final String PARSE_ATTRIBUTE_DESCRIPTION = "description";
    public static final String PARSE_ATTRIBUTE_DINNER_TIME = "dinnerTime";
    public static final String PARSE_ATTRIBUTE_DRUG_DESCRIPTION = EXTRA_DRUG_DESCRIPTION;
    public static final String PARSE_ATTRIBUTE_DRUG_ID = "drugId";
    public static final String PARSE_ATTRIBUTE_EMERGENCY_PHONE_NUMBER = "emergencyPhoneNumber";
    public static final String PARSE_ATTRIBUTE_EXACT_TIME = "exactTime";
    public static final String PARSE_ATTRIBUTE_HOURS = "Hours";
    public static final String PARSE_ATTRIBUTE_IS_AFFECTED_BY_MEDICATION_FACTOR = "isAffectedByMedicationFactor";
    public static final String PARSE_ATTRIBUTE_IS_REMINDER_ACTIVE = "isReminderActive";
    public static final String PARSE_ATTRIBUTE_LEVEL = "level";
    public static final String PARSE_ATTRIBUTE_LOCALE_SPAIN = "isoCountryCode";
    public static final String PARSE_ATTRIBUTE_LOG_TAG = "logTag";
    public static final String PARSE_ATTRIBUTE_LUNCH_TIME = "lunchTime";
    public static final String PARSE_ATTRIBUTE_MESSAGE = "message";
    public static final String PARSE_ATTRIBUTE_NON_HCB_USER = "nonHCBUser";
    public static final String PARSE_ATTRIBUTE_NOT_SET = "_NOT_SET";
    public static final String PARSE_ATTRIBUTE_OBJECT_ID = "objectId";
    public static final String PARSE_ATTRIBUTE_ORIGIN_FROM = "originFrom";
    public static final String PARSE_ATTRIBUTE_PATIENT = "patient";
    public static final String PARSE_ATTRIBUTE_PERCENTAGE = "percentage";
    public static final String PARSE_ATTRIBUTE_Q1 = "Q1";
    public static final String PARSE_ATTRIBUTE_Q2 = "Q2";
    public static final String PARSE_ATTRIBUTE_Q4 = "Q4";
    public static final String PARSE_ATTRIBUTE_Q5 = "Q5";
    public static final String PARSE_ATTRIBUTE_QUESTION_ONE = "One";
    public static final String PARSE_ATTRIBUTE_QUESTION_ONE_SUB = "One_sub";
    public static final String PARSE_ATTRIBUTE_QUESTION_TWO = "Two";
    public static final String PARSE_ATTRIBUTE_QUESTION_THREE = "Three";
    public static final String PARSE_ATTRIBUTE_QUESTION_THREE_SUB = "Three_sub";
    public static final String PARSE_ATTRIBUTE_QUESTION_FOUR = "Four";
    public static final String PARSE_ATTRIBUTE_QUESTION_FIVE = "Five";
    public static final String PARSE_ATTRIBUTE_QUESTION_SIX = "Six";
    public static final String PARSE_ATTRIBUTE_QUESTION_SEVEN = "Seven";
    public static final String PARSE_ATTRIBUTE_QUESTION_EIGHT = "Eight";
    public static final String PARSE_ATTRIBUTE_QUESTION_NINE = "Nine";
    public static final String PARSE_ATTRIBUTE_QUESTION_PREFFIX = "Q";
    public static final String PARSE_ATTRIBUTE_RELATION_TO_USER_IN_LIFE_EVENT = "user";
    public static final String PARSE_ATTRIBUTE_RELATION_TO_USER = "User";
    public static final String PARSE_ATTRIBUTE_SAVED_AT = "savedAt";
    public static final String PARSE_ATTRIBUTE_SUB_QUESTION_PREFFIX = "SQ";
    public static final String PARSE_ATTRIBUTE_SUB_QUESTION_VALUE_SEPARATOR = "_";
    public static final String PARSE_ATTRIBUTE_SCORE = "Score";
    public static final String PARSE_ATTRIBUTE_STACKTRACE = "stackTrace";
    public static final String PARSE_ATTRIBUTE_APP_VERSION = "appVersion";
    public static final String PARSE_ATTRIBUTE_SYMPTOM = "symptom";
    public static final String PARSE_ATTRIBUTE_SYMPTOM_DEPRESSIVE = "SymptomDepressive";
    public static final String PARSE_ATTRIBUTE_SYMPTOM_MANIAC = "SymptomManiac";
    public static final String PARSE_ATTRIBUTE_SYMPTOM_MIXED = "SymptomMixed";
    public static final String PARSE_ATTRIBUTE_TAKEN = "taken";
    public static final String PARSE_ATTRIBUTE_TEST_HOUR = "testHour";
    public static final String PARSE_ATTRIBUTE_THERAPIST_EMAIL = "therapistEmail";
    public static final String PARSE_ATTRIBUTE_TIME_TABLE_DESCRIPTION = EXTRA_TIME_TABLE_DESCRIPTION;
    public static final String PARSE_ATTRIBUTE_TIME_TABLE_ID = "timeTableId";
    public static final String PARSE_ATTRIBUTE_TO_EMAIL = "toEmail";
    public static final String PARSE_ATTRIBUTE_TROPHY_DESCRIPTION = "description";
    public static final String PARSE_ATTRIBUTE_TROPHY_SHOWED_AT = "showedAt";
    public static final String PARSE_ATTRIBUTE_TROPHY_UPGRADEABLE = "upgradeable";
    public static final String PARSE_ATTRIBUTE_TYPE = "type";
    public static final String PARSE_ATTRIBUTE_UPDATED_AT = "updatedAt";
    public static final String PARSE_ATTRIBUTE_USERNAME = "username";
    public static final String PARSE_ATTRIBUTE_USER_AS_USERNAME = "user";
    public static final String PARSE_ATTRIBUTE_WEIGHTED = "Weighted";
    public static final String PARSE_ATTRIBUTE_WEEK_TEST_DAY = "weekTestDay";
    public static final String NO_USER_LOGGED_IN = "NO_USER_LOGGED_IN";
    public static final String FROM_BACKGROUND_SERVICE = "FROM_BACKGROUND_SERVICE";


    private static final List<String> WEB_VIEW_URLS_WITHOUT_PROGRESS_BAR = Arrays.asList("track.php",
            "secure.gravatar.com", "googlesyndication.com", "skimresources.com",
            "scorecardresearch.com", "www.google.com/ads", "doubleckick.net",
            "afyn11.net", "googleadservices.com", "googletagservices.com",
            "adroll.com", "advertising", "mookiel.com", "maps.googleapis.com",
            "developers.google.com");
    public static final String PARSE_ATTRIBUTE_DURATION_IN_SECONDS = "durationInSeconds";
    public static final String USER_PREFERENCE_LIFELOG_AUTHORIZED = "USER_PREFERENCE_LIFELOG_AUTHORIZED";
    public static final String USER_PREFERENCE_SLEEP_MINUTES = "USER_PREFERENCE_SLEEP_HOURS";
    public static final String USER_PREFERENCE_PHYSICAL_ACTIVITIES_MINUTES = "USER_PREFERENCE_PHYSICAL_ACTIVITIES";
    public static final String DEFAULT_CHARACTER_TIME_SPLIT = ":";
    public static final String QUESTION_MARK = "?";
    public static final String EQUALS_OPERATOR = "=";
    public static final String AMPERSAND = "&";
    public static final String PARSE_ATTRIBUTE_IS_AFFECTED_BY_LIFELOG_FACTOR = "isAffectedByLifelogFactor";
    public static final String PARSE_ATTRIBUTE_HOURS_FOR_ENERGY_SCORE = "hoursForEnergyScore";
    public static final int DEFAULT_ENERGY_SCORE = 0;
    public static final int PSEUDO_QUESTION_NUMBER_LIFELOG = -1;
    public static final String PARSE_ATTRIBUTE_CATEGORY = "category";
    public static final String PARSE_ATTRIBUTE_ADVICE = "advice";
    public static final String PARSE_ATTRIBUTE_INDEX = "indice";
    public static final String PARSE_ATTRIBUTE_ENABLED = "enabled";
    public static final int MAX_CAPSULE_ROWS = 1000;
    public static final String PARSE_CLASS_CUSTOM_DRUG = "CustomDrug";
    public static final String PARSE_ATTRIBUTE_CUSTOM_DRUG_DESCRIPTION = "description";
    public static final String EXTRA_RELOAD_MEDICATIONS = "reloadMedications";

    public static final String DEFAULT_CASULE = "El trastorno bipolar tiende a presentarse con <b>más de un episodio a lo largo de la vida</b>, por lo que decimos que es crónico y recurrente. Se combinan episodios agudos (hipomanía, manía o depresión) con períodos libres de síntomas en los que la persona puede llevar la vida que desee apoyándose en un tratamiento farmacológico adecuado para mantener esta estabilidad combinado con intervenciones psicológicas, si así lo requiere.";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String LATEST_SESSION_TOKEN = "latestSessionToken";
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String APP_NEWS = "appNews";
    public static final String CURRENT_RELEASES = "currentReleases";
    public static final String USER_PREFERENCE_APP_NEWS = "_appNews";
    public static final String USER_PREFERENCE_APP_NEWS_SHOWED_TIME = "_appNewsShowed";
    public static final String HTML_SCALED_FONT_SIZE = "3";
    public static final String PARSE_ATTRIBUTE_LANG = "lang";
    public static final String DEFAULT_LANGUAGE = "es";
    public static final long DEFAULT_DELAY_TO_CLOSE_INACTIVE_ACTIVITY = 5l;
    public static final String LAST_TIME_VERSION_CHECKED = "LAST_TIME_VERSION_CHECKED";
    public static final String PARSE_CLASS_EMAIL_ALERT = "EmailAlert";
    public static final Object EMAIL_QUEUED = "QUEUED";
    public static final String PARSE_ATTRIBUTE_STATUS = "status";
    public static final String PARSE_CLASS_PACKAGE_CATEGORY = "PackageCategory";
    public static final String PARSE_ATTRIBUTE_PACKAGENAME = "packagename";
    public static final String PACKAGE_COMODIN = "COMODIN";
    public static final String SIN_CLASIFICAR = "SIN_CLASIFICAR";


    public static List<String> getWebViewUrlsWithoutProgressBar() {
        return WEB_VIEW_URLS_WITHOUT_PROGRESS_BAR;
    }

    private Constants() {
         /*Porque los constructores no tienen que tener instancias...
         Y menos esta clase de constantes...*/
    }
}
