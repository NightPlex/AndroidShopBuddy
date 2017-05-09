package com.school.shopbudd.applogic.shoppinglists.entity;

import com.school.shopbudd.api.database.DAOAbstract;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class DefaultShoppingListDAO extends DAOAbstract<ShoppingListEntity> implements ShoppingListDAO
{
    @Override
    public Long save(ShoppingListEntity entity)
    {
        return saveOrUpdate(entity);
    }

    @Override
    public ShoppingListEntity getById(Long id)
    {
        return getById(id, ShoppingListEntity.class);
    }

    @Override
    public List<ShoppingListEntity> getAllEntities()
    {
        return getAllEntities(ShoppingListEntity.class);
    }

    @Override
    public Boolean deleteById(Long id)
    {
        return deleteById(id, ShoppingListEntity.class);
    }
}
