package com.school.shopbudd.ui.shoppinglist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.school.shopbudd.R;
import com.school.shopbudd.api.context.InstanceFactory;
import com.school.shopbudd.api.context.InstanceFactoryAbstract;
import com.school.shopbudd.applogic.product.ProductService;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import com.school.shopbudd.main.ActivityCache;
import com.school.shopbudd.main.MainActivity;
import com.school.shopbudd.utils.LineUtil;
import com.school.shopbudd.utils.MessageUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class EditDeleteListDialog extends DialogFragment {

    private ActivityCache cache;
    private ListItem listItem;
    private ShoppingListService shoppingListService;
    private ProductService productService;


    public static EditDeleteListDialog newEditDeleteInstance(ListItem item, ActivityCache cache) {

        EditDeleteListDialog dialogFragment = getEditDeleteFragment(item, cache);
        return dialogFragment;
    }


    private static EditDeleteListDialog getEditDeleteFragment(ListItem item, ActivityCache cache) {
        EditDeleteListDialog dialogFragment = new EditDeleteListDialog();
        dialogFragment.setCache(cache);
        dialogFragment.setListItem(item);
        return dialogFragment;
    }

    public void setCache(ActivityCache cache) {
        this.cache = cache;
    }

    public void setListItem(ListItem listItem) {
        this.listItem = listItem;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        InstanceFactoryAbstract instanceFactory = new InstanceFactory(cache.getActivity().getApplicationContext());
        shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
        productService = (ProductService) instanceFactory.createInstance(ProductService.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.shopping_list_actions, null);
        Button editButton = (Button) rootView.findViewById(R.id.edit);
        Button duplicateButton = (Button) rootView.findViewById(R.id.duplicate);
        Button resetButton = (Button) rootView.findViewById(R.id.reset);
        Button shareButton = (Button) rootView.findViewById(R.id.share);
        Button deleteButton = (Button) rootView.findViewById(R.id.delete);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.title);

        String listDialogTitle = getContext().getResources().getString(R.string.list_as_title, listItem.getListName());
        titleTextView.setText(listDialogTitle);

        editButton.setOnClickListener(getEditOnClickListener());
        duplicateButton.setOnClickListener(getDuplicateOnClickListener());
        deleteButton.setOnClickListener(getDeleteOnClickListener());
        resetButton.setOnClickListener(getResetCheckedItemsOnClickListener());
        shareButton.setOnClickListener(getShareOnClickListener());

        builder.setView(rootView);
        return builder.create();
    }

    private View.OnClickListener getDeleteOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                MessageUtil.showAlertDialog(
                        getContext(),
                        R.string.delete_confirmation_title,
                        R.string.delete_list_confirmation,
                        listItem.getListName(),
                        deleteList());
            }
        };
    }

    private View.OnClickListener getEditOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (!ListDialogFragment.isOpened()) {
                    DialogFragment productFragment = ListDialogFragment.newEditInstance(listItem, cache);
                    productFragment.show(cache.getActivity().getSupportFragmentManager(), "List");
                }
            }
        };
    }

    private View.OnClickListener getShareOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                List<ProductItem> productItems = new ArrayList<>();
                Context context = getContext();
                productService.getAllProducts(listItem.getId())
                        .doOnNext(productItem -> productItems.add(productItem))
                        .doOnCompleted(() ->
                        {
                            String shareableText = shoppingListService.getShareableText(listItem, productItems);
                            MessageUtil.shareText(context, shareableText, listItem.getListName());
                        })
                        .subscribe();

            }
        };
    }

    private View.OnClickListener getDuplicateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                productService.duplicateProducts(listItem.getId())
                        .doOnCompleted(() ->
                        {
                            MainActivity activity = (MainActivity) cache.getActivity();
                            activity.updateListView();
                        })
                        .subscribe();

            }
        };
    }

    private View.OnClickListener getResetCheckedItemsOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                productService.resetCheckedProducts(listItem.getId())
                        .doOnCompleted(() ->
                        {
                            MainActivity activity = (MainActivity) cache.getActivity();
                            activity.updateListView();
                        })
                        .subscribe();
            }
        };
    }

    private Observable<Void> deleteList() {
        Observable observable = Observable
                .defer(() -> Observable.just(deleteListSync()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void deleteListSync() {
        String id = listItem.getId();
        shoppingListService.deleteById(id)
                .doOnCompleted(() ->
                {
                    MainActivity activity = (MainActivity) cache.getActivity();
                    activity.updateListView();
                })
                .subscribe();
        productService.deleteAllFromList(id).subscribe();
        return null;
    }

//   TODO:     // delete notification if any
//        NotificationUtils.removeNotification(cache.getActivity(), id);
//        ReminderReceiver alarm = new ReminderReceiver();
//        Intent intent = new Intent(cache.getActivity(), ReminderSchedulingService.class);
//        alarm.cancelAlarm(cache.getActivity(), intent, id);
//        return null;


}