package com.school.shopbudd.api.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.school.shopbudd.api.model.ItemAbstract;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public abstract class ViewHolderAbstract <Item extends ItemAbstract, Cache extends CachePoly> extends RecyclerView.ViewHolder
{
    protected Cache cache;

    public ViewHolderAbstract(final View parent, Cache cache)
    {
        super(parent);
        this.cache = cache;
    }

    public abstract void processItem(Item item);
}
