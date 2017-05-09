package com.school.shopbudd.api.database;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public enum Database {
    APP("ShopBudd.db", 7); // for the app

    private String dbName;
    private int dbVersion;

    Database(String dbName, int dbVersion) {
        this.dbName = dbName;
        this.dbVersion = dbVersion;
    }

    public String getDbName() {
        return dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }
}
