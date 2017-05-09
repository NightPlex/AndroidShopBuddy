package com.school.shopbudd.api.context;

import android.content.Context;
import com.school.shopbudd.api.database.Database;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class InstanceFactory extends InstanceFactoryAbstract
{
    public InstanceFactory(Context context)
    {
        super(context);
    }

    @Override
    protected Database getDB()
    {
        return Database.APP;
    }
}
