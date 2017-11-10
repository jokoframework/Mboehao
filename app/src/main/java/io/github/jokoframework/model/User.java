package io.github.jokoframework.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User {
    private Long id;
    private String username;
    private String email;
    private Boolean active;
    private String personId;
    private String name;
    private String surName;
    private String birthDate;
    private String status;
    private String phone;
    private Boolean pdv;
    private String lastAccessDate;
    private String clientReferenceNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getPdv() {
        return pdv;
    }

    public void setPdv(Boolean pdv) {
        this.pdv = pdv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(String lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getClientReferenceNumber() {
        return clientReferenceNumber;
    }

    public void setClientReferenceNumber(String clientReferenceNumber) {
        this.clientReferenceNumber = clientReferenceNumber;
    }

    public String getPrettyName() {
        return name + " " + surName;
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
        User rhs = (User) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.id, rhs.id)
                .append(this.username, rhs.username)
                .append(this.email, rhs.email)
                .append(this.active, rhs.active)
                .append(this.personId, rhs.personId)
                .append(this.name, rhs.name)
                .append(this.surName, rhs.surName)
                .append(this.birthDate, rhs.birthDate)
                .append(this.status, rhs.status)
                .append(this.phone, rhs.phone)
                .append(this.pdv, rhs.pdv)
                .append(this.clientReferenceNumber, rhs.clientReferenceNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(id)
                .append(username)
                .append(email)
                .append(active)
                .append(personId)
                .append(name)
                .append(surName)
                .append(birthDate)
                .append(status)
                .append(phone)
                .append(pdv)
                .append(clientReferenceNumber)
                .toHashCode();
    }
}
