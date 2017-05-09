package com.school.shopbudd.applogic.product.model;

import com.school.shopbudd.utils.LineUtil;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class AutoComplete {
    private Set<String> stores;
    private Set<String> categories;
    private Set<String> products;

    public AutoComplete() {
        stores = new TreeSet<>();
        categories = new TreeSet<>();
        products = new TreeSet<>();
    }

    public void updateLists(ProductItem item) {
        String name = item.getProductName();
        String store = item.getProductStore();
        String category = item.getProductCategory();

        if (!LineUtil.isEmpty(name)) {
            products.add(name);
        }

        if (!LineUtil.isEmpty(category)) {
            categories.add(category);
        }

        if (!LineUtil.isEmpty(store)) {
            stores.add(store);
        }
    }

    public Set<String> getStores() {
        return stores;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public Set<String> getProducts() {
        return products;
    }

    public String[] getProductsArray() {
        return getStringArray(this.products);
    }

    public String[] getStoresArray() {
        return getStringArray(this.stores);
    }

    public String[] getCategoryArray() {
        return getStringArray(this.categories);
    }

    private String[] getStringArray(Set<String> aSet) {
        String[] stringArray = new String[aSet.size()];
        aSet.toArray(stringArray);
        return stringArray;
    }

    public void copyTo(AutoComplete autoCompleteLists) {
        autoCompleteLists.stores = this.stores;
        autoCompleteLists.categories = this.categories;
        autoCompleteLists.products = this.products;
    }
}
