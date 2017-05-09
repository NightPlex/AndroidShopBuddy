package com.school.shopbudd.api.database;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.school.shopbudd.api.context.ContextSet;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */

//Got the idea from: http://stackoverflow.com/questions/26782583/implement-generic-abstract-entity-class-with-dao-interface-and-implemantation
public class DAOAbstract<T extends EntityAbstract> implements ContextSet {
    private static final String SUCCESSFUL = "successful";
    private static final String GET_ALL_ENTITIES = "getAllEntities";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String GET_DAO = "getDao";
    private static final String SAVE_OR_UPDATE = "saveOrUpdate";
    private static final String GET_BY_ID = "getById";
    private static final String ID = ", ID=";
    private static final String ENTITY = ", ENTITY=";
    private DatabaseHelper database;

    @Override
    public void setContext(Context context, Database db) {
        database = new DatabaseHelper(context, db);
    }

    protected Long saveOrUpdate(T entity) {
        try {
            @SuppressWarnings("unchecked")
            Dao<T, Long> dao = database.getDao((Class<T>) entity.getClass());
            dao.createOrUpdate(entity);
            return entity.getId();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected T getById(Long id, Class<T> type) {
        try {
            Dao<T, Long> dao = database.getDao(type);
            T entity = dao.queryForId(id);
            return entity;
        } catch (SQLException e) {
            return null;
        }
    }

    protected List<T> getAllEntities(Class<T> type) {
        List<T> entities;
        try {
            Dao<T, Long> dao = database.getDao(type);
            entities = dao.queryForAll();
            return entities;
        } catch (SQLException e) {
            return null;
        }
    }

    protected boolean deleteById(Long id, Class<T> type) {
        try {
            Dao<T, Long> dao = database.getDao(type);
            dao.deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    protected Dao<T, Long> getDao(Class<T> type) {
        try {
            Dao<T, Long> dao = database.getDao(type);
            return dao;
        } catch (SQLException e) {
            return null;
        }
    }


}