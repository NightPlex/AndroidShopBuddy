package com.school.shopbudd.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import com.school.shopbud.R;
import com.school.shopbudd.R;
import com.school.shopbudd.api.context.InstanceFactory;
import com.school.shopbudd.api.context.InstanceFactoryAbstract;
import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
import com.school.shopbudd.base.BaseActivity;
import com.school.shopbudd.main.listener.AddOnClickListener;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 */

public class MainActivity extends BaseActivity {

    public static final String LIST_ID_KEY = "list.id";
    private ShoppingListService shoppingListService;
    private ActivityCache cache;
    private Subscriber<Long> alertUpdateSubscriber;
    private Subscription alertSubscriber;

    private boolean menusVisible;


    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InstanceFactoryAbstract instanceFactory = new InstanceFactory(getApplicationContext());
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
        cache = new ActivityCache(this);
        menusVisible = false;

//        getApplicationContext().deleteDatabase(DB.APP.getDbName());

        updateListView();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPref.getString(SettingConsts.LIST_SORT_BY, null);
        if (sortBy == null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            //TODO: editor.putString(SettingConsts.LIST_SORT_BY, PFAComparators.SORT_BY_NAME);
            editor.putBoolean(SettingConsts.LIST_SORT_ASCENDING, true);
            editor.commit();
        }

        cache.getNewListFab().setOnClickListener(new AddOnClickListener(cache));
        setupAlertSubscriber();

        overridePendingTransition(0, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem deleteItem = menu.findItem(R.id.imageview_delete);
        deleteItem.setOnMenuItemClickListener(new ShowDeleteListsOnClickListener(cache));

        sortItem.setVisible(menusVisible);
        deleteItem.setVisible(menusVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_main;
    }

    public void updateListView() {
        List<ListItem> allListItems = new ArrayList<>();

        shoppingListService.getAllListItems()
                .doOnNext(item -> allListItems.add(item))
                .doOnCompleted(() ->
                {
                    if (allListItems.isEmpty()) {
                        cache.getNoListsLayout().setVisibility(View.VISIBLE);
                        subscribeAlert();
                    } else {
                        cache.getNoListsLayout().setVisibility(View.GONE);
                        unsubscribeAlert();
                    }

                    menusVisible = !allListItems.isEmpty();
                    invalidateOptionsMenu();

                    // sort according to last sort selection
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    String sortBy = sharedPref.getString(SettingsKeys.LIST_SORT_BY, PFAComparators.SORT_BY_NAME);
                    boolean sortAscending = sharedPref.getBoolean(SettingsKeys.LIST_SORT_ASCENDING, true);
                    shoppingListService.sortList(allListItems, sortBy, sortAscending);

                    cache.getListAdapter().setList(allListItems);
                    cache.getListAdapter().notifyDataSetChanged();
                })
                .subscribe();
    }

    private void subscribeAlert() {
        alertSubscriber = Observable.interval(1L, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(x -> alertUpdateSubscriber.onNext(x))
                .subscribe();
    }

    private void unsubscribeAlert() {
        if (alertSubscriber != null && !alertSubscriber.isUnsubscribed()) {
            cache.getAlertTextView().setVisibility(View.GONE);
            alertSubscriber.unsubscribe();
        }
    }

    private void setupAlertSubscriber() {
        alertUpdateSubscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long time) {
                if (time % 2 != 0) {
                    cache.getAlertTextView().setVisibility(View.GONE);
                } else {
                    cache.getAlertTextView().setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public void reorderListView(List<ListItem> sortedList) {
        cache.getListAdapter().setList(sortedList);
        cache.getListAdapter().notifyDataSetChanged();
    }
}
