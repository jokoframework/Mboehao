package io.github.jokoframework.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM country")
    List<Country> getAll();

    //@Query("SELECT * FROM country where first_name LIKE  :firstName AND last_name LIKE :lastName")
    //Country findByName(String firstName, String lastName);

    //@Query("SELECT COUNT(*) from country")
    //int countCountries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCountry(Country country);

    //@Insert
    //void insertAll(Country... countries);

    //@Delete
    //void delete(Country country);

    //@Query("delete from country where cid = :countryId")
    //int deleteCountriesById(String countryId);

    //@Query("delete from country where first_name like :badName OR last_name like :badName")
    //int deleteUsersByName(String badName);

    @Query("delete FROM country")
    void deleteAll();



}
