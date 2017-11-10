package io.github.jokoframework.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import io.github.jokoframework.mboehaolib.model.DefaultResponse;


public class LoginResponse extends DefaultResponse {

    private String secret;
    private Long expiration;
    private String userId;
    private User user;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoginResponse that = (LoginResponse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(secret, that.secret)
                .append(expiration, that.expiration)
                .append(userId, that.userId)
                .append(user, that.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(secret)
                .append(expiration)
                .append(userId)
                .append(user)
                .toHashCode();
    }
}
