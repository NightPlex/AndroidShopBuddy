package com.school.shopbudd.main;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import com.school.shopbudd.R;
import com.school.shopbudd.api.ui.CachePoly;
import com.school.shopbudd.ui.shoppinglist.ListAdapter;

import java.util.ArrayList;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ActivityCache extends CachePoly{
    private AppCompatActivity activity;
    private FloatingActionButton newListFab;
    private ListAdapter listAdapter;
    private LinearLayout noListsLayout;
    private LinearLayout alertTextView;

    public ActivityCache(AppCompatActivity activity) {
        this.activity = activity;

        listAdapter = new ListAdapter(new ArrayList<>(), this);

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(listAdapter);

        newListFab = (FloatingActionButton) activity.findViewById(R.id.fab_new_list);
        alertTextView = (LinearLayout) activity.findViewById(R.id.insert_alert);

        noListsLayout = (LinearLayout) activity.findViewById(R.id.no_lists_layout);
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public FloatingActionButton getNewListFab() {
        return newListFab;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    public LinearLayout getNoListsLayout() {
        return noListsLayout;
    }

    public LinearLayout getAlertTextView() {
        return alertTextView;
    }
}
