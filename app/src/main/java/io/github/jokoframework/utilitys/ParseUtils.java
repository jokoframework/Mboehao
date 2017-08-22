package io.github.jokoframework.utilitys;


import android.content.Context;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;
import io.github.jokoframework.aplicationconstants.Constants;


public class ParseUtils {

    private static final String LOG_TAG = ParseUtils.class.getName();
    private static ParseACL DEFAULT_ACL = new ParseACL();

    static {
        DEFAULT_ACL.setPublicReadAccess(false);
        DEFAULT_ACL.setPublicWriteAccess(false);
    }

    private ParseUtils() {
    }

    public static String getParameterValue(Context context, String parameterName) {
        String value = null;
        if (Utils.isNetworkAvailable(context)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_PARAMETER);
            query.whereEqualTo(Constants.PARSE_PARAMETER_DESCRIPTION, parameterName);
            try {
                List<ParseObject> results = query.find();
                if (results != null && !results.isEmpty()) {
                    //El 0 significa traer el primero, que es el único que nos interesa en caso de
                    //que haya varias coincidencias
                    ParseObject urlFromParse = results.get(0);
                    value = urlFromParse.getString(Constants.PARSE_PARAMETER_VALUE);
                }
            } catch (ParseException e) {
                Log.e(LOG_TAG, "No se pudo consultar el valor del parámetro: " + parameterName);
            }
        }
        return value;
    }

    public static ParseObject getObjectById(Context pContext, String pClassName, String pObjectId) {
        ParseObject objectById = null;
        if (Utils.isNetworkAvailable(pContext)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(pClassName);
            query.whereEqualTo(Constants.PARSE_ATTRIBUTE_OBJECT_ID, pObjectId);
            try {
                List<ParseObject> results = query.find();
                if (results != null && !results.isEmpty()) {
                    //El 0 significa traer el primero
                    objectById = results.get(0);
                }
            } catch (ParseException e) {
                Log.e(LOG_TAG, String.format("No se pudo consultar el valor del objeto %s: id -> %s ", pClassName, pObjectId));
            }
        }
        return objectById;
    }

    /**
     * Retorna true si el usuario no pertenece al HCB
     *
     * @param currentUser
     * @return
     */

    public static boolean isNonHCBUser(ParseUser currentUser) {
        return currentUser != null
                && currentUser.getBoolean(Constants.PARSE_ATTRIBUTE_NON_HCB_USER);
    }

    public static Double getAverageForQuestion(String pQuestionId, Integer pDaysRangeStart, Integer pDaysCount) {
        Double average = 0.0;
        final ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_CLASS_DAILY_TEST);
        //Calculamos el promedio de la última semana
        // 14 días = 1209600000
        query.whereGreaterThanOrEqualTo(Constants.PARSE_ATTRIBUTE_SAVED_AT,
                new Date(new Date().getTime() - Constants.ONE_DAY * pDaysRangeStart));
        // 7 días = 604800000
        query.whereLessThanOrEqualTo(Constants.PARSE_ATTRIBUTE_SAVED_AT,
                new Date(new Date().getTime() - Constants.ONE_DAY * pDaysCount));
        query.orderByAscending(Constants.PARSE_ATTRIBUTE_SAVED_AT);
        query.whereEqualTo(Constants.PARSE_ATTRIBUTE_RELATION_TO_USER, currentUser);
        try {
            int scoresLastWeekCount = 0;
            double cumulative = 0.0;
            List<ParseObject> scoreList = query.find();
            for (ParseObject aScoreList : scoreList) {
                scoresLastWeekCount++;
                //Pregunta de la Energía del usuario
                final double currentLoopEnergyValue = aScoreList.getDouble(Constants.PARSE_ATTRIBUTE_Q2);
                cumulative += currentLoopEnergyValue;
            }
            if (scoresLastWeekCount > 0) {
                average = cumulative / scoresLastWeekCount;
            }
        } catch (ParseException e) {
            Log.e(LOG_TAG, String.format("Error calculando promedio para %s", pQuestionId), e);
        }
        return average;
    }


    public static ParseACL getDefaultAcl(ParseUser currentUser) {
        DEFAULT_ACL.setReadAccess(currentUser, true);
        DEFAULT_ACL.setWriteAccess(currentUser, true);
        return DEFAULT_ACL;
    }
}
