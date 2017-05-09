package com.school.shopbudd.ui.product.adapter;

import android.preference.PreferenceManager;
import android.view.View;
import com.school.shopbudd.R;
import com.school.shopbudd.api.ui.AdapterAbstract;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.main.SettingConsts;
import com.school.shopbudd.ui.product.ProductActivityCache;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ProductAdapter extends AdapterAbstract<ProductItem, ProductActivityCache, ProductItemViewHolder> {

    public ProductAdapter(List<ProductItem> productsList, ProductActivityCache cache) {
        super(
                productsList,
                cache,
                R.layout.product_list_item);
        setLayoutId(getListItemLayout());
    }

    private int getListItemLayout() {
        int listItemLayout;
        if (PreferenceManager.getDefaultSharedPreferences(cache.getActivity()).getBoolean(SettingConsts.CHECKBOX_POSITION_PREF, true)) {
            listItemLayout = R.layout.product_list_item;
        } else {
            listItemLayout = R.layout.product_list_item_left_hand;
        }
        return listItemLayout;
    }

    @Override
    protected ProductItemViewHolder createNewViewHolder(View view) {
        return new ProductItemViewHolder(view, cache);
    }
}
