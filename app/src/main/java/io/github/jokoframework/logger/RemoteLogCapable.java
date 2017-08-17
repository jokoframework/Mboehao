package io.github.jokoframework.logger;


import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface RemoteLogCapable {

    void flushLogs(List<Map<String,Object>> pLogsToRepository);
}
