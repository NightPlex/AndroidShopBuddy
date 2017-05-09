package com.school.shopbudd.ui.shoppinglist.adapter;

import android.view.View;
import com.school.shopbudd.R;
import com.school.shopbudd.ui.shoppinglist.dialog.ListDialogCache;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ListDialogFocusListener implements View.OnFocusChangeListener
{
    private ListDialogCache dialogCache;

    public ListDialogFocusListener(ListDialogCache dialogCache)
    {
        this.dialogCache = dialogCache;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if ( hasFocus )
        {
            dialogCache.getDeadlineLayout().setVisibility(View.GONE);
            dialogCache.getDeadlineExpansionButton().setImageResource(R.drawable.ic_keyboard_arrow_down_white_48sp);
        }
    }
}
