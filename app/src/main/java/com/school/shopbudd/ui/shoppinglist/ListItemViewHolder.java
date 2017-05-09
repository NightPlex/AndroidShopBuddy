package com.school.shopbudd.ui.shoppinglist;

import android.app.DialogFragment;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.school.shopbudd.R;
import com.school.shopbudd.api.context.InstanceFactory;
import com.school.shopbudd.api.context.InstanceFactoryAbstract;
import com.school.shopbudd.api.ui.ViewHolderAbstract;
import com.school.shopbudd.applogic.product.ProductService;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.applogic.product.model.TotalItem;
import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import com.school.shopbudd.main.ActivityCache;
import com.school.shopbudd.main.MainActivity;
import com.school.shopbudd.main.SettingConsts;
import com.school.shopbudd.utils.LineUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ListItemViewHolder extends ViewHolderAbstract<ListItem, ActivityCache> {
    private static final String HIGH_PRIORITY_INDEX = "0";
    private ListItemCache listItemCache;
    private ProductService productService;
    private ShoppingListService shoppingListService;

    ListItemViewHolder(final View parent, ActivityCache cache) {
        super(parent, cache);
        this.listItemCache = new ListItemCache(parent);
        InstanceFactoryAbstract instanceFactory = new InstanceFactory(cache.getActivity());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
    }

    public void processItem(ListItem item) {
        listItemCache.getListNameTextView().setText(item.getListName());
        listItemCache.getDeadLineTextView().setText(item.getDeadlineDate());

        List<ProductItem> productItems = new ArrayList<>();
        productService.getAllProducts(item.getId())
                .filter(productItem -> !productItem.isChecked())
                .doOnNext(productItem -> productItems.add(productItem))
                .doOnCompleted(() ->
                {
                    int reminderStatus = shoppingListService.getReminderStatusResource(item, productItems);
                    listItemCache.getReminderBar().setImageResource(reminderStatus);
                })
                .subscribe();

        setupPriorityIcon(item);
        setupReminderIcon(item);

        final TotalItem[] totalItem = new TotalItem[1];
        productService.getInfo(item.getId())
                .doOnNext(result -> totalItem[0] = result)
                .doOnCompleted(() ->
                        {
                            listItemCache.getListDetails().setText(
                                    totalItem[0].getInfo(listItemCache.getCurrency(), cache.getActivity()) +
                                            item.getDetailInfo(listItemCache.getListCard().getContext()));
                            listItemCache.getNrProductsTextView().setText(String.valueOf(totalItem[0].getNrProducts()));
                        }
                ).subscribe();

        listItemCache.getListCard().setOnClickListener(v ->
        {
            Intent intent = new Intent(cache.getActivity(), ProductsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MainActivity.LIST_ID_KEY, item.getId());
            cache.getActivity().startActivity(intent);
        });

        listItemCache.getListCard().setOnLongClickListener(view ->
        {

            DialogFragment editDeleteFragment = EditDeleteListDialog.newEditDeleteInstance(item, cache);
            editDeleteFragment.show(cache.getActivity().getSupportFragmentManager(), "List");

            return true;
        });

        ImageButton showDetailsButton = listItemCache.getShowDetailsImageButton();
        showDetailsButton.setOnClickListener(v ->
        {
            listItemCache.setDetailsVisible(!listItemCache.isDetailsVisible());
            if (listItemCache.isDetailsVisible()) {
                showDetailsButton.setImageResource(R.drawable.ic_keyboard_arrow_up_white_48sp);
                listItemCache.getListDetails().setVisibility(View.VISIBLE);

            } else {
                showDetailsButton.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48sp);
                listItemCache.getListDetails().setVisibility(View.GONE);
            }
        });

    }

    private void setupReminderIcon(ListItem item) {
        if (LineUtil.isEmpty(item.getReminderCount())) {
            listItemCache.getReminderImageView().setVisibility(View.GONE);
        } else {
            listItemCache.getReminderImageView().setVisibility(View.VISIBLE);
            AppCompatActivity activity = cache.getActivity();
            if (!PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(SettingConsts.NOTIFICATIONS_ENABLED, true)) {
                listItemCache.getReminderImageView().setColorFilter(ContextCompat.getColor(activity, R.color.red));
            } else {
                listItemCache.getReminderImageView().setColorFilter(ContextCompat.getColor(activity, R.color.middlegrey));
            }
        }
    }

    private void setupPriorityIcon(ListItem item) {
        if (HIGH_PRIORITY_INDEX.equals(item.getPriority())) {
            listItemCache.getHighPriorityImageView().setVisibility(View.VISIBLE);
        } else {
            listItemCache.getHighPriorityImageView().setVisibility(View.GONE);
        }
    }


}