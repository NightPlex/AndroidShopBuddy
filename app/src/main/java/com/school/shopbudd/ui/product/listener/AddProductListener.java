package com.school.shopbudd.ui.product.listener;

import android.view.View;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.ui.product.ProductActivityCache;
import com.school.shopbudd.ui.product.dialog.ProductDialogFragment;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class AddProductListener implements View.OnClickListener
{
    private ProductActivityCache cache;

    public AddProductListener(ProductActivityCache cache)
    {
        this.cache = cache;
    }

    @Override
    public void onClick(View v)
    {
        ProductItem item = new ProductItem();
        if ( !ProductDialogFragment.isOpened() )
        {
            ProductDialogFragment productDialogFragment = ProductDialogFragment.newAddDialogInstance(item, cache);
            productDialogFragment.show(cache.getActivity().getSupportFragmentManager(), "ProductDialog");
        }
    }
}
