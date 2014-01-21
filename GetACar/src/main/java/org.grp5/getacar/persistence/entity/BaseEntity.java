package org.grp5.getacar.persistence.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Abstract base entity from which all other entities should extend.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private Integer id;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false,
            columnDefinition = "int(10) unsigned NOT NULL AUTO_INCREMENT")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    // TODO: Version!

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .omitNullValues()
                .add("id", id).toString();
    }
}
