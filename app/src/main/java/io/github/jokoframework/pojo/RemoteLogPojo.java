package io.github.jokoframework.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import io.github.jokoframework.constants.AppConstants;

public class RemoteLogPojo extends BasePojo {
    private String logTag;
    private String message;
    private String stackTrace;
    private String username;
    private String level;
    private Date savedAt;
    public static final int BUILD_VERSION = AppConstants.VERSION_CODE;
    private String appVersion;

    public RemoteLogPojo(String level, String tag, String msg, String stackTraceString) {
        setLevel(level);
        setLogTag(tag);
        setMessage(msg);
        setStackTrace(stackTraceString);
        setSavedAt(new Date());
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getAppVersion() {
        return appVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("logTag", logTag)
                .append("message", message)
                .append("stackTrace", stackTrace)
                .append("username", username)
                .append("level", level)
                .append("savedAt", savedAt)
                .append("appVersion", appVersion)
                .toString();
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
