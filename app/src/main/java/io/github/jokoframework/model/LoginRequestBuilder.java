package io.github.jokoframework.model;

import java.util.Map;

public class LoginRequestBuilder {

    private LoginRequest data = new LoginRequest();

    public LoginRequestBuilder username(String username) {
        data.setUsername(username);
        return this;
    }

    public LoginRequestBuilder password(String password) {
        data.setPassword(password);
        return this;
    }


    public LoginRequest build() {
        return data;
    }
}
