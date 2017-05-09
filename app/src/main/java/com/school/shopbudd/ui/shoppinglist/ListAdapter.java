package com.school.shopbudd.ui.shoppinglist;

import android.preference.PreferenceManager;
import android.view.View;
import com.school.shopbudd.R;
import com.school.shopbudd.api.ui.AdapterAbstract;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import com.school.shopbudd.main.ActivityCache;
import com.school.shopbudd.main.SettingConsts;

import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ListAdapter extends AdapterAbstract<ListItem, ActivityCache, ListItemViewHolder> {
    public ListAdapter(List<ListItem> shoppingList, ActivityCache cache) {
        super(
                shoppingList,
                cache,
                R.layout.shopping_list_item);

        setLayoutId(getListItemLayout());
    }

    private int getListItemLayout() {
        int listItemLayout;
        if (PreferenceManager.getDefaultSharedPreferences(cache.getActivity()).getBoolean(SettingConsts.CHECKBOX_POSITION_PREF, true)) {
            listItemLayout = R.layout.shopping_list_item;
        } else {
            listItemLayout = R.layout.shopping_list_item_left_hand;
        }
        return listItemLayout;
    }

    @Override
    protected ListItemViewHolder createNewViewHolder(View view) {
        return new ListItemViewHolder(view, cache);
    }
}