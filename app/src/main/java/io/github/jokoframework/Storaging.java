package io.github.jokoframework;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by joaquin on 10/08/17.
 */

public interface Storaging {

    public void getDataFrom(FindCallback<ParseObject> findCallback);
    public void setDataFrom(String description, SaveCallback saveCallback);

}
