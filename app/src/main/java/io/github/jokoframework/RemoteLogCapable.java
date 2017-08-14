package io.github.jokoframework;


import com.parse.ParseObject;

import java.util.List;
import java.util.Map;

public interface RemoteLogCapable {

    public void flushLogs(final List<ParseObject> pLogsToParse);

//    public void flushLogs(List<Map<String,String>> pLogsToParse);
}
