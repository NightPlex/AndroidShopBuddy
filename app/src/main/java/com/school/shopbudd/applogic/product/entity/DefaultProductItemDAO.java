package com.school.shopbudd.applogic.product.entity;

import com.school.shopbudd.api.database.DAOAbstract;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class DefaultProductItemDAO extends DAOAbstract<ProductEntity> implements ProductItemDAO {
    @Override
    public Long save(ProductEntity entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public ProductEntity getById(Long id) {
        return getById(id, ProductEntity.class);
    }

    @Override
    public List<ProductEntity> getAllEntities() {
        return getAllEntities(ProductEntity.class);
    }

    @Override
    public Boolean deleteById(Long id) {
        return deleteById(id, ProductEntity.class);
    }
}
