package io.github.jokoframework;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class ParseSave implements Storaging {

    @Override
    public void getDataFrom(FindCallback<ParseObject> findCallback) {
        ParseQuery.getQuery(Constants.PARSE_DATA_SAVE_ENTRY)
                .whereEqualTo(Constants.PARSE_ATTRIBUTE_USERNAME, ParseUser.getCurrentUser())
                .orderByAscending(Constants.PARSE_ATTRIBUTE_DESCRIPTION)
                .findInBackground(findCallback);
    }

    @Override
    public void setDataFrom(String description, SaveCallback saveCallback) {
        ParseObject parseEntry = new ParseObject(Constants.PARSE_DATA_SAVE_ENTRY);
        parseEntry.put(Constants.PARSE_ATTRIBUTE_DESCRIPTION, description);
        parseEntry.put(Constants.PARSE_ATTRIBUTE_USERNAME, ParseUser.getCurrentUser());
        List<ParseObject> list = Arrays.asList(parseEntry);
        ParseObject.saveAllInBackground(list,saveCallback);
    }
}
