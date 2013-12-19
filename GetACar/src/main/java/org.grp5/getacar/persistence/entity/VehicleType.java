package org.grp5.getacar.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * VehicleType entity class.
 */
@Entity
@Table(name = "fahrzeugtyp")
@AttributeOverride(name = "id", column = @Column(name = "ft_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class VehicleType extends BaseEntity {

    private String name;
    private String description;

    @Basic(optional = false)
    @Column(name = "ft_name", columnDefinition = "varchar(100)", unique = true)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = type;
    }

    @Basic(optional = false)
    @Column(name = "ft_beschreibung", columnDefinition = "text")
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String comment) {
        this.description = comment;
    }
}