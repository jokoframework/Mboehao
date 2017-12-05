package io.github.jokoframework.utilities;


import android.content.Context;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import io.github.jokoframework.constants.AppConstants;


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
        if (AppUtils.isNetworkAvailable(context)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(AppConstants.PARSE_PARAMETER);
            query.whereEqualTo(AppConstants.PARSE_PARAMETER_DESCRIPTION, parameterName);
            try {
                List<ParseObject> results = query.find();
                if (results != null && !results.isEmpty()) {
                    //El 0 significa traer el primero, que es el único que nos interesa en caso de
                    //que haya varias coincidencias
                    ParseObject urlFromParse = results.get(0);
                    value = urlFromParse.getString(AppConstants.PARSE_PARAMETER_VALUE);
                }
            } catch (ParseException e) {
                Log.e(LOG_TAG, "No se pudo consultar el valor del parámetro: " + parameterName);
            }
        }
        return value;
    }

    public static ParseObject getObjectById(Context pContext, String pClassName, String pObjectId) {
        ParseObject objectById = null;
        if (AppUtils.isNetworkAvailable(pContext)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(pClassName);
            query.whereEqualTo(AppConstants.PARSE_ATTRIBUTE_OBJECT_ID, pObjectId);
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


    public static ParseACL getDefaultAcl(ParseUser currentUser) {
        if (currentUser != null){
            DEFAULT_ACL.setReadAccess(currentUser, true);
            DEFAULT_ACL.setWriteAccess(currentUser, true);
        }
        return DEFAULT_ACL;
    }

    public static ParseUser getCurrentUser() {
        ParseUser user;
        try {
            user = ParseUser.getCurrentUser();
        } catch (NullPointerException nullException) {
            Log.e(LOG_TAG, "Parse no inicializado");
            user = null;
        }
        return user;
    }
}
