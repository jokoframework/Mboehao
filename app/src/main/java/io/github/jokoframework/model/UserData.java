package io.github.jokoframework.model;

import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

import io.github.jokoframework.mboehaolib.constants.Constants;

public class UserData {
    /**
     * Instance variables
     */
    private long id;
    private String username;
    private Boolean active;
    private long expiration;
    private String refreshToken;
    private String userAccessToken;
    private String firstName;
    private String surName;
    private String clientReferenceNumber;
    private User user;
    private SharedPreferences sharedPreferences;
    private Boolean rememberMe;
    private Boolean showTagMessage;

    public UserData(String username, Boolean active, long expiration, String refreshToken,
                    String userAccessToken, User user, SharedPreferences sharedPreferences) {
        this.username = username;
        this.active = active;
        this.expiration = expiration;
        this.refreshToken = refreshToken;
        this.userAccessToken = userAccessToken;
        this.user = user;
        this.sharedPreferences = sharedPreferences;
        this.rememberMe = false;
        this.showTagMessage = true;
    }

    public UserData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        loadUser();
    }

    public boolean isLogged() {
        return refreshToken != null;
    }

    public void logout() {
        active = null;
        refreshToken = null;
        expiration = 0L;
        firstName = null;
        surName = null;
        clientReferenceNumber = null;
        id = 0L;
        showTagMessage = true;
        persistUser();
    }

    public void persistUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_REFRESH_TOKEN, refreshToken);
        editor.putLong(Constants.KEY_EXPIRATION, expiration);
        editor.putBoolean(Constants.KEY_REMEMBER_ME, rememberMe);
        if (this.rememberMe) {
            editor.putString(Constants.KEY_USERNAME, username);
        }
        editor.putBoolean(Constants.KEY_ACTIVE, active != null && active);
        editor.putString(Constants.KEY_FIRST_NAME, firstName);
        editor.putString(Constants.KEY_SURNAME, surName);
        editor.putString(Constants.KEY_CLIENT_REFERENCE_NUMBER, clientReferenceNumber);
        editor.putLong(Constants.KEY_USER_ID, id);
        editor.putBoolean(Constants.KEY_SHOW_TAG_MESSAGE, showTagMessage);
        editor.apply();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getClientReferenceNumber() {
        return clientReferenceNumber;
    }

    public void setClientReferenceNumber(String clientReferenceNumber) {
        this.clientReferenceNumber = clientReferenceNumber;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public Boolean getShowTagMessage() {
        return showTagMessage;
    }

    public void setShowTagMessage(Boolean showTagMessage) {
        this.showTagMessage = showTagMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserData userData = (UserData) o;

        return new EqualsBuilder()
                .append(expiration, userData.expiration)
                .append(username, userData.username)
                .append(active, userData.active)
                .append(refreshToken, userData.refreshToken)
                .append(user, userData.user)
                .append(clientReferenceNumber, userData.clientReferenceNumber)
                .append(sharedPreferences, userData.sharedPreferences)
                .isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(active)
                .append(expiration)
                .append(refreshToken)
                .append(user)
                .append(clientReferenceNumber)
                .append(sharedPreferences)
                .toHashCode();
    }

    private void loadUser() {
        setRefreshToken(sharedPreferences
                .getString(Constants.KEY_REFRESH_TOKEN, null));
        setUsername(sharedPreferences
                .getString(Constants.KEY_USERNAME, null));
        setActive(sharedPreferences
                .getBoolean(Constants.KEY_ACTIVE, true));
        setExpiration(sharedPreferences
                .getLong(Constants.KEY_EXPIRATION, 0l));
        setId(sharedPreferences.getLong(Constants.KEY_USER_ID, 0L));
        setClientReferenceNumber(sharedPreferences.getString(Constants.KEY_CLIENT_REFERENCE_NUMBER, null));
        setRememberMe(sharedPreferences.getBoolean(Constants.KEY_REMEMBER_ME, true));
        setShowTagMessage(sharedPreferences.getBoolean(Constants.KEY_SHOW_TAG_MESSAGE, true));
    }


    public boolean isValid() {
        boolean valid = false;
        valid = getExpiration() > new Date().getTime();
        if (!valid) {
            valid = StringUtils.isNoneBlank(getClientReferenceNumber(), getRefreshToken());
        }
        return valid;
    }
}

