package com.school.shopbudd.applogic.product.converter;

import com.school.shopbudd.api.context.ContextSet;
import com.school.shopbudd.applogic.product.entity.ProductEntity;
import com.school.shopbudd.applogic.product.model.ProductItem;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ProductConvertorService extends ContextSet
{
    void convertItemToEntity(ProductItem item, ProductEntity entity);

    void convertEntitiesToItem(ProductEntity entity, ProductItem item);

    String getDoubleAsString(Double price);

    Double getStringAsDouble(String priceString);
}
