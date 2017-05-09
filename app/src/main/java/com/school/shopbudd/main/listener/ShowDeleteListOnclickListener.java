package com.school.shopbudd.main.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.school.shopbudd.main.ActivityCache;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ShowDeleteListOnclickListener implements MenuItem.OnMenuItemClickListener {
    private ActivityCache cache;

    public ShowDeleteListOnclickListener(ActivityCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        AppCompatActivity activity = cache.getActivity();
        Intent intent = new Intent(activity, DeleteListsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        return true;
    }
}
