package com.school.shopbudd.ui.shoppinglist;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.school.shopbudd.R;
import com.school.shopbudd.main.SettingConsts;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ListItemCache {
    private TextView listNameTextView;
    private TextView nrProductsTextView;
    private TextView deadLineTextView;
    private TextView listDetails;
    private ImageView reminderBar;
    private CardView listCard;
    private ImageView highPriorityImageView;
    private ImageView reminderImageView;
    private ImageButton showDetailsImageButton;
    private boolean detailsVisible;
    private String currency;

    public ListItemCache(View parent) {
        listNameTextView = (TextView) parent.findViewById(R.id.textview_list_name);
        deadLineTextView = (TextView) parent.findViewById(R.id.textview_list_info);
        nrProductsTextView = (TextView) parent.findViewById(R.id.textview_prod_quantity);
        listDetails = (TextView) parent.findViewById(R.id.textview_list_details);
        listCard = (CardView) parent.findViewById(R.id.cardview_item);
        highPriorityImageView = (ImageView) parent.findViewById(R.id.imageview_high_prio_icon);
        reminderImageView = (ImageView) parent.findViewById(R.id.imageview_reminder_icon);
        reminderBar = (ImageView) parent.findViewById(R.id.reminder_bar);
        showDetailsImageButton = (ImageButton) parent.findViewById(R.id.expand_button_list);
        detailsVisible = false;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        String defaultCurrency = parent.getResources().getString(R.string.currency);
        currency = sharedPreferences.getString(SettingConsts.CURRENCY, defaultCurrency);
    }

    public TextView getListNameTextView() {
        return listNameTextView;
    }

    public TextView getNrProductsTextView() {
        return nrProductsTextView;
    }

    public TextView getDeadLineTextView() {
        return deadLineTextView;
    }

    public CardView getListCard() {
        return listCard;
    }

    public ImageView getHighPriorityImageView() {
        return highPriorityImageView;
    }

    public ImageView getReminderBar() {
        return reminderBar;
    }

    public ImageButton getShowDetailsImageButton() {
        return showDetailsImageButton;
    }

    public TextView getListDetails() {
        return listDetails;
    }

    public ImageView getReminderImageView() {
        return reminderImageView;
    }

    public boolean isDetailsVisible() {
        return detailsVisible;
    }

    public void setDetailsVisible(boolean detailsVisible) {
        this.detailsVisible = detailsVisible;
    }

    public String getCurrency() {
        return currency;
    }
}
