package com.school.shopbudd.api.context;

import android.content.Context;
import com.school.shopbudd.api.database.Database;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ContextSet {
    void setContext(Context context, Database db);
}
