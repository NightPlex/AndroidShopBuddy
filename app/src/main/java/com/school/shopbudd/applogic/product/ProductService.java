package com.school.shopbudd.applogic.product;

import com.school.shopbudd.api.context.ContextSet;
import com.school.shopbudd.applogic.product.model.AutoComplete;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.applogic.product.model.TotalItem;
import rx.Observable;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public interface ProductService extends ContextSet {
    Observable<Void> saveOrUpdate(ProductItem item, String listId);

    Observable<Void> duplicateProducts(String listId);

    Observable<Void> copyToList(ProductItem product, String listId);

    Observable<Void> resetCheckedProducts(String listId);

    Observable<ProductItem> getById(String id);

    String getProductImagePath(String id);

    Observable<Void> deleteById(String id);

    Observable<Void> deleteOnlyImage(String id);

    Observable<Void> deleteSelected(List<ProductItem> productItems);

    Observable<ProductItem> getAllProducts(String listId);

    Observable<ProductItem> getAllProducts();

    Observable<TotalItem> getInfo(String listId);

    Observable<Void> deleteAllFromList(String listId);

    List<ProductItem> moveSelectedToEnd(List<ProductItem> productItems);

    TotalItem computeTotals(List<ProductItem> productItems);

    Boolean isSearched(String[] searchedTexts, ProductItem item);

    String getSharableText(ProductItem item);

    Observable<AutoComplete> getAutoCompleteListsObservable();

    Observable<Boolean> deleteInvisibleProductsFromDb(List<String> listIds);

}
