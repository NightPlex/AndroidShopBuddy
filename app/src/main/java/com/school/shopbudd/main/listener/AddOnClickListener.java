package com.school.shopbudd.main.listener;

import android.view.View;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import com.school.shopbudd.main.ActivityCache;
import com.school.shopbudd.ui.shoppinglist.dialog.ListDialogFragment;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class AddOnClickListener implements View.OnClickListener {
    private ActivityCache cache;

    public AddOnClickListener(ActivityCache cache) {
        this.cache = cache;

    }

    @Override
    public void onClick(View v) {
        ListItem item = new ListItem();
        String priority = "1";
        item.setPriority(priority);

        if (!ListDialogFragment.isOpened()) {
            ListDialogFragment listDialogFragment = ListDialogFragment.newAddInstance(item, cache);
            listDialogFragment.show(cache.getActivity().getSupportFragmentManager(), "DialogFragment");
        }
    }
}
