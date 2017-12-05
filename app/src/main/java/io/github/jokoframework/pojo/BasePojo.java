package io.github.jokoframework.pojo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.jokoframework.constants.AppConstants;


public class BasePojo {

    private String objectId;

    private static final String LOG_TAG = BasePojo.class.getSimpleName();

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = BasePojo.getGson();
        return gson.toJson(this);
    }

    @NonNull
    private static Gson getGson() {
        return new GsonBuilder().setDateFormat(AppConstants.DATE_TIME_FORMAT_ISO_8601).create();
    }

    public static <T extends BasePojo> T deserialize(String serializedData, Class<? extends BasePojo> clazz) {
        T pojo;
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = getGson();
        pojo = (T) gson.fromJson(serializedData, clazz);
        return pojo;
    }

    public static <T extends BasePojo> List<T> deserializeList(String arrayString, Type type) {
        List<T> list = new ArrayList<>();
        if (StringUtils.isNotBlank(arrayString)) {
            Gson gson = new Gson();
            try {
                list = gson.fromJson(arrayString, type);
            } catch (JsonSyntaxException exception) {
                list = new ArrayList<>();
                Log.e(LOG_TAG, String.format("Error deserializando el json para: %s. %s ", exception.getMessage(), type), exception);
            }
        }
        return list;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int compareTo(BasePojo basePojo) {
        int equals = 1;
        if (basePojo != null) {
            if (getObjectId() != null) {
                equals = getObjectId().compareTo(basePojo.getObjectId());
            } else {
                equals = toString().compareTo(basePojo.toString());
            }
        }
        return equals;
    }
}