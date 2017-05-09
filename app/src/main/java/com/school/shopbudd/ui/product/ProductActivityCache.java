package com.school.shopbudd.ui.product;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.school.shopbudd.R;
import com.school.shopbudd.api.ui.CachePoly;
import com.school.shopbudd.main.SettingConsts;
import com.school.shopbudd.ui.product.adapter.ProductAdapter;

import java.util.ArrayList;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ProductActivityCache extends CachePoly {
    private AppCompatActivity activity;
    private FloatingActionButton newListFab;
    private ProductAdapter productsAdapter;
    private TextView totalAmountTextView;
    private TextView totalCheckedTextView;
    private TextView currencyTextView;
    private LinearLayout totalLayout;
    private RecyclerView recyclerView;
    private String listId;
    private String listName;
    private Boolean statisticsEnabled;
    private LinearLayout noProductsLayout;
    private TextInputLayout searchTextInputLayout;
    private AutoCompleteTextView searchAutoCompleteTextView;
    private ImageButton cancelSarchButton;
    private LinearLayout alertTextView;

    public ProductActivityCache(AppCompatActivity activity, String listId, String listName, boolean statisticsEnabled) {
        this.activity = activity;
        this.listId = listId;
        this.listName = listName;
        this.statisticsEnabled = statisticsEnabled;

        productsAdapter = new ProductAdapter(new ArrayList<>(), this);

        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(productsAdapter);

        newListFab = (FloatingActionButton) activity.findViewById(R.id.fab_new_product);
        totalAmountTextView = (TextView) activity.findViewById(R.id.textview_total_amount);
        totalCheckedTextView = (TextView) activity.findViewById(R.id.textview_total_checked);
        totalLayout = (LinearLayout) activity.findViewById(R.id.layout_total);
        currencyTextView = (TextView) activity.findViewById(R.id.textview_currency);
        noProductsLayout = (LinearLayout) activity.findViewById(R.id.no_products_layout);
        searchTextInputLayout = (TextInputLayout) activity.findViewById(R.id.search_input_layout);
        searchAutoCompleteTextView = (AutoCompleteTextView) activity.findViewById(R.id.search_input_text);
        cancelSarchButton = (ImageButton) activity.findViewById(R.id.cancel_search);
        alertTextView = (LinearLayout) activity.findViewById(R.id.insert_alert);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String defaultCurrency = activity.getResources().getString(R.string.currency);
        String currency = sharedPreferences.getString(SettingConsts.CURRENCY, defaultCurrency);
        currencyTextView.setText(currency);
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public FloatingActionButton getNewListFab() {
        return newListFab;
    }

    public ProductAdapter getProductsAdapter() {
        return productsAdapter;
    }

    public String getListId() {
        return listId;
    }

    public TextView getTotalAmountTextView() {
        return totalAmountTextView;
    }

    public TextView getTotalCheckedTextView() {
        return totalCheckedTextView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public LinearLayout getTotalLayout() {
        return totalLayout;
    }

    public Boolean getStatisticsEnabled() {
        return statisticsEnabled;
    }

    public String getListName() {
        return listName;
    }

    public LinearLayout getNoProductsLayout() {
        return noProductsLayout;
    }

    public TextInputLayout getSearchTextInputLayout() {
        return searchTextInputLayout;
    }

    public AutoCompleteTextView getSearchAutoCompleteTextView() {
        return searchAutoCompleteTextView;
    }

    public ImageButton getCancelSarchButton() {
        return cancelSarchButton;
    }

    public LinearLayout getAlertTextView() {
        return alertTextView;
    }
}
