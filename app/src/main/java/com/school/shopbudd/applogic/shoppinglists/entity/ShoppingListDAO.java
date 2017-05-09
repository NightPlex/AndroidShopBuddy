package com.school.shopbudd.applogic.shoppinglists.entity;

import com.school.shopbudd.api.context.ContextSet;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ShoppingListDAO extends ContextSet

{
    Long save(ShoppingListEntity entity);

    ShoppingListEntity getById(Long id);

    List<ShoppingListEntity> getAllEntities();

    Boolean deleteById(Long id);
}
