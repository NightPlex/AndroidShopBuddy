package com.school.shopbudd.applogic.shoppinglists.converter;

import com.school.shopbudd.api.context.ContextSet;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListEntity;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ShoppingListConverter extends ContextSet
{
    void convertItemToEntity(ListItem item, ShoppingListEntity entity);

    void convertEntityToItem(ShoppingListEntity entity, ListItem item);
}
