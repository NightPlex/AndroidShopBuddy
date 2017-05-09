package com.school.shopbudd.injection;

import dagger.Module;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
@Module(
        includes = {
                // DEPENDENCY_INJECTION add all Modules here
                ProductModule.class,
                ShoppingListModule.class,
        }
)
public class AppContextModule implements AppModule {
}

interface AppModule
{
        // to encapsulate all Context Modules
}


