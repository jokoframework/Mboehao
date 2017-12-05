package io.github.jokoframework.model;

import android.os.Build;

import com.facebook.AccessToken;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

import io.github.jokoframework.constants.AppConstants;
import io.github.jokoframework.mboehaolib.constants.Constants;


public class LoginRequest {

    private String username;
    private String password;
    private Map<String, Object> custom = new HashMap<>();

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected LoginRequest() {
    }

    public static LoginRequestBuilder builder() {
        return new LoginRequestBuilder();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Map<String, Object> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        LoginRequest rhs = (LoginRequest) obj;
        return new EqualsBuilder()
                .append(this.username, rhs.username)
                .append(this.password, rhs.password)
                .append(this.custom, rhs.custom)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(username)
                .append(password)
                .append(custom)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", password)
                .append("custom", custom)
                .toString();
    }

    public static LoginRequest builder(AccessToken fbAccessToken) {
        LoginRequest loginRequest = new LoginRequest();
        if (fbAccessToken != null) {
            final Map<String, Object> custom = loginRequest.getCustom();
            loginRequest.setUsername(fbAccessToken.getUserId());
            loginRequest.setPassword(AppConstants.DEFAULT_DEMO_ACCCESS_CODE);
            custom.put(AppConstants.DEVICE_TYPE, Constants.DEVICE_TYPE);
            custom.put(AppConstants.DEVICE_NAME, Build.MODEL);
            custom.put(Constants.LOGIN_PROVIDER, Constants.LOGIN_PROVIDER_FACEBOOK);
            custom.put(Constants.ACCESS_TOKEN, fbAccessToken.getToken());
        }
        return loginRequest;
    }
}
