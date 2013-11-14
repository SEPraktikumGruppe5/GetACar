package org.grp5.getacar.util;

/**
 * Enumeration used when role names are needed in the code to prevent typos.
 * Role names here MUST therefore match the ones in the database.
 */
public enum UserRole {
    ADMIN("Admin"), USER("Benutzer");

    private final String nameInDB;

    private UserRole(String nameInDB) {
        this.nameInDB = nameInDB;
    }

    public String getNameInDB() {
        return nameInDB;
    }
}