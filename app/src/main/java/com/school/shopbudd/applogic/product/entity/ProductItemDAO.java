package com.school.shopbudd.applogic.product.entity;

import com.school.shopbudd.api.context.ContextSet;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ProductItemDAO extends ContextSet {
    Long save(ProductEntity entity);

    ProductEntity getById(Long id);

    List<ProductEntity> getAllEntities();

    Boolean deleteById(Long id);
}