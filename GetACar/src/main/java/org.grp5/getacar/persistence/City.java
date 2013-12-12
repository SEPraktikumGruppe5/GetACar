package org.grp5.getacar.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * City entity class.
 */
@Entity
@Table(name = "stadt")
@AttributeOverride(name = "id", column = @Column(name = "s_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class City extends BaseEntity {

    private String name;
    private String asciiName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String country;
    private Integer population;

    @Basic(optional = false)
    @Column(name = "s_name", columnDefinition = "varchar(200)")
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic(optional = false)
    @Column(name = "s_asciiname", columnDefinition = "varchar(200)")
    @NotNull
    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }


    @Basic(optional = false)
    @Column(name = "s_breitengrad", columnDefinition = "decimal(10,7)")
    @NotNull
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Basic(optional = false)
    @Column(name = "s_laengengrad", columnDefinition = "decimal(10,7)")
    @NotNull
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic(optional = false)
    @Column(name = "s_land", columnDefinition = "varchar(2)")
    @NotNull
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic(optional = false)
    @Column(name = "s_bevoelkerung", columnDefinition = "int(11)")
    @NotNull
    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
