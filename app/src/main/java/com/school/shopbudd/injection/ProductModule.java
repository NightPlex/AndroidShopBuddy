package com.school.shopbudd.injection;

import com.school.shopbudd.applogic.product.ProductService;
import com.school.shopbudd.applogic.product.converter.DefaultProductConvertorService;
import com.school.shopbudd.applogic.product.converter.ProductConvertorService;
import com.school.shopbudd.applogic.product.entity.DefaultProductItemDAO;
import com.school.shopbudd.applogic.product.entity.ProductItemDAO;
import com.school.shopbudd.applogic.product.model.DefaultProductService;
import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
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
        includes = {
                ShoppingListModule.class
        },
        injects = {
                ProductService.class,
                ProductItemDAO.class,
                ProductConvertorService.class,
        }
)
public class ProductModule implements AppModule {
    @Provides
    @Singleton
    ProductItemDAO provideProductItemDao() {
        return new DefaultProductItemDAO();
    }

    @Provides
    @Singleton
    ProductConvertorService provideProductConverterService() {
        return new DefaultProductConvertorService();
    }

    @Provides
    @Singleton
    ProductService provideProductService(
            ProductItemDAO productItemDao,
            ProductConvertorService converterService,
            ShoppingListService shoppingListService
    ) {
        return new DefaultProductService(
                productItemDao,
                converterService,
                shoppingListService
        );
    }
}