package org.grp5.getacar.domain;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract base domain object from which all domain objects should extend.
 */
@MappedSuperclass
public abstract class BaseDomainObject {

    private Integer id;

    @Id
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
        if (!(o instanceof BaseDomainObject)) return false;

        BaseDomainObject that = (BaseDomainObject) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

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
