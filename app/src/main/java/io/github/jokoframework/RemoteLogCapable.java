package io.github.jokoframework;


import java.util.List;
import java.util.Map;

public interface RemoteLogCapable {

//    public void flushLogs(final List<ParseObject> pLogsToParse);

    void flushLogs(List<Map<String,Object>> pLogsToRepository);
}
