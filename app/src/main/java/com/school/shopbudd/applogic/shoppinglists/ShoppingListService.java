package com.school.shopbudd.applogic.shoppinglists;

import com.school.shopbudd.api.context.ContextSet;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListEntity;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import org.joda.time.DateTime;
import rx.Observable;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * Main service for our shopping lists ( not products.)
 *
 * @author NightPlex
 */
public interface ShoppingListService extends ContextSet
{
    Observable<Void> saveOrUpdate(ListItem item);

    Void saveOrUpdateSync(ListItem item);

    Observable<ListItem> getById(String id);

    DateTime getReminderDate(ListItem item);

    DateTime getDeadLine(ListItem item);

    int getReminderStatusResource(ListItem item, List<ProductItem> productItems);

    ShoppingListEntity getEntityByIdSync(String id);

    Observable<Void> deleteById(String id);

    Observable<ListItem> getAllListItems();

    Observable<String> deleteSelected(List<ListItem> shoppingListItems);

    List<ListItem> moveSelectedToEnd(List<ListItem> shoppingListItems);

    String getShareableText(ListItem listItem, List<ProductItem> productItems);
}
