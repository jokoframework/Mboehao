package io.github.jokoframework.mboehaolib.model;

import android.os.Parcelable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


public class BaseDTO {
    /**
     * Atributo que almacenará datos no tipados (genéricos) del tipo clave-valor.
     *
     */
    private Map<String, Parcelable> additionalProperties = new HashMap<>();

    public Map<String, Parcelable> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Parcelable> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public void setAdditionalProperty(String name, Parcelable value) {
        this.additionalProperties.put(name, value);
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
        BaseDTO rhs = (BaseDTO) obj;
        return new EqualsBuilder()
                .append(this.additionalProperties, rhs.additionalProperties)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(additionalProperties)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("additionalProperties", additionalProperties)
                .toString();
    }
}
