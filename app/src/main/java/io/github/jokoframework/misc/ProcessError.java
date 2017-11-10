package io.github.jokoframework.misc;

import io.github.jokoframework.model.UserAccessResponse;


public interface ProcessError {
    void afterProcessError(UserAccessResponse response);
    void afterProcessError(String message);
    void afterProcessErrorNoConnection();
}
