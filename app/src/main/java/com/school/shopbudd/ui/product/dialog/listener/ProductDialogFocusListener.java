package com.school.shopbudd.ui.product.dialog.listener;

import android.view.View;
import com.school.shopbudd.R;
import com.school.shopbudd.ui.product.dialog.ProductDialogCache;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ProductDialogFocusListener implements View.OnFocusChangeListener
{
    private ProductDialogCache dialogCache;

    public ProductDialogFocusListener(ProductDialogCache cache)
    {
        this.dialogCache = cache;
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        if ( hasFocus )
        {
            dialogCache.getExpandableLayout().setVisibility(View.GONE);
            dialogCache.getExpandableImageView().setImageResource(R.drawable.ic_keyboard_arrow_down_white_48sp);
        }
    }
}
