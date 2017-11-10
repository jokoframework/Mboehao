package io.github.jokoframework.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import io.github.jokoframework.mboehaolib.model.BaseDTO;


public class UserAccessResponse extends BaseDTO{

    private Boolean success;
    private String errorCode;
    private String message;
    private String secret;
    private Long expiration;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAccessResponse that = (UserAccessResponse) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(success, that.success)
                .append(errorCode, that.errorCode)
                .append(message, that.message)
                .append(secret, that.secret)
                .append(expiration, that.expiration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(success)
                .append(errorCode)
                .append(message)
                .append(secret)
                .append(expiration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("success", success)
                .append("errorCode", errorCode)
                .append("message", message)
                .append("secret", secret)
                .append("expiration",expiration)

                .toString();
    }

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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
