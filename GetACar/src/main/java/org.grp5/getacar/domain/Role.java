package org.grp5.getacar.domain;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Role domain object.
 */
@Entity
@Table(name = "rolle")
@AttributeOverride(name = "id", column = @Column(name = "ro_id",
        columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class Role extends BaseDomainObject {

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
