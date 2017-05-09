package com.school.shopbudd.api.context;

import android.content.Context;
import com.school.shopbudd.api.database.Database;
import com.school.shopbudd.injection.AppContextModule;
import dagger.ObjectGraph;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public abstract class InstanceFactoryAbstract {
    private static Context context;

    public InstanceFactoryAbstract(Context context) {
        InstanceFactoryAbstract.context = context;
    }

    public Object createInstance(Class aClass) {
        ObjectGraph objectGraph = ObjectGraph.create(new AppContextModule());
        Object classInstance = objectGraph.get(aClass);
        ((ContextSet) classInstance).setContext(context, getDB());
        return classInstance;
    }

    public static Object createInstance(Class aClass, Database db) {
        ObjectGraph objectGraph = ObjectGraph.create(new AppContextModule());
        Object classInstance = objectGraph.get(aClass);
        ((ContextSet) classInstance).setContext(context, db);
        return classInstance;
    }

    protected abstract Database getDB();
}
