package io.github.jokoframework.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "country")
public class Country {

    //@PrimaryKey(autoGenerate = true)
    private int cid;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "country_name")
    private String countryName;

    @ColumnInfo(name = "country_code")
    private String countryCode;

    public int getCid() { return cid; }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
