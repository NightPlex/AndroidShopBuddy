package com.school.shopbudd.injection;

import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
import com.school.shopbudd.applogic.shoppinglists.converter.DefaultShoppingListConvertor;
import com.school.shopbudd.applogic.shoppinglists.entity.DefaultShoppingListDAO;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListDAO;
import com.school.shopbudd.applogic.shoppinglists.converter.ShoppingListConverter;
import com.school.shopbudd.applogic.shoppinglists.model.DefaultShoppingListService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
@Module(
        injects = {
                ShoppingListDAO.class,
                ShoppingListService.class,
                ShoppingListConverter.class
        }
)
public class ShoppingListModule implements AppModule
{

    @Provides
    @Singleton
    ShoppingListDAO provideShoppingListDao()
    {
        return new DefaultShoppingListDAO();
    }

    @Provides
    @Singleton
    ShoppingListConverter provideShoppingListConverter()
    {
        return new DefaultShoppingListConvertor();
    }

    @Provides
    @Singleton
    ShoppingListService provideShoppingListService(
            ShoppingListDAO shoppingListDao,
            ShoppingListConverter shoppingListConverter
    )
    {
        return new DefaultShoppingListService(
                shoppingListDao,
                shoppingListConverter
        );
    }
}
