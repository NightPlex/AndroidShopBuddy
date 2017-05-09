package com.school.shopbudd.api.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.school.shopbudd.R;
import com.school.shopbudd.applogic.product.entity.ProductEntity;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String ON_CREATE = "onCreate";
    private static final String START = "start";
    private static final String ON_UPGRADE = "onUpgrade";
    private static final String ORMLITE_CONFIG_TXT = "ormlite_config.txt";
    private static List<Class<? extends EntityAbstract>> entityClasses;

    public DatabaseHelper(Context context, Database db) {
        super(context, db.getDbName(), null, db.getDbVersion(), R.raw.ormlite_config);
    }

    private void setupClasses() {
        // SETUP_PERSISTENCE: add all Entity clases to this list
        entityClasses = new ArrayList<>();
        entityClasses.add(ProductEntity.class);
        entityClasses.add(ShoppingListEntity.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, final ConnectionSource connectionSource) {
        try {
            setupClasses();
            for (Class aClass : entityClasses) {
                TableUtils.createTable(connectionSource, aClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            setupClasses();
            for (Class aClass : entityClasses) {
                TableUtils.dropTable(connectionSource, aClass, true);
            }
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class DataBaseConfig extends OrmLiteConfigUtil {
        // SETUP_PERSISTENCE: run every time the DB schema changes

        /**
         * Run Configuration: make sure to set "privacy-friendly-shopping-list/app/src/main" as Working directory
         */
        public static void main(String[] args) throws Exception {
            writeConfigFile(ORMLITE_CONFIG_TXT);
        }
    }
}
