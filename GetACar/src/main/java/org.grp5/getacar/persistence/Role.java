package org.grp5.getacar.persistence;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Role entity class.
 */
@Entity
@Table(name = "rolle")
@AttributeOverride(name = "id", column = @Column(name = "ro_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
public class Role extends BaseEntity {

    private String name;

    @Basic(optional = false)
    @Column(name = "ro_name", columnDefinition = "varchar(75)", unique = true)
    @Size(min = 2, max = 75)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
