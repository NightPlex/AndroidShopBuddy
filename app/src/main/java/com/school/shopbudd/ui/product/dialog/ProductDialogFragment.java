package com.school.shopbudd.ui.product.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.school.shopbudd.R;
import com.school.shopbudd.api.context.InstanceFactory;
import com.school.shopbudd.api.context.InstanceFactoryAbstract;
import com.school.shopbudd.applogic.product.ProductService;
import com.school.shopbudd.applogic.product.model.AutoComplete;
import com.school.shopbudd.applogic.product.model.ProductItem;
import com.school.shopbudd.ui.CameraActivity;
import com.school.shopbudd.ui.product.PhotoPreviewActivity;
import com.school.shopbudd.ui.product.ProductActivity;
import com.school.shopbudd.ui.product.ProductActivityCache;
import com.school.shopbudd.ui.product.dialog.listener.PriceInputFilter;
import com.school.shopbudd.ui.product.dialog.listener.ProductDialogFocusListener;
import com.school.shopbudd.utils.LineUtil;
import com.school.shopbudd.utils.MessageUtil;
import rx.Observable;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ProductDialogFragment extends DialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 2;
    private static final int REQUEST_PHOTO_PREVIEW_FROM_DIALOG = 3;
    private static final double MAX_PRICE_ALLOWED = 10000000;
    public static final String DEFAULT_QUANTITY = "1";

    private static boolean opened;
    private static boolean editDialog;
    private static boolean resetState;
    private static boolean saveConfirmed;

    private ProductItem item;
    private ProductDialogCache dialogCache;
    private ProductActivityCache cache;

    private ProductService productService;

    public static ProductDialogFragment newEditDialogInstance(ProductItem item, ProductActivityCache cache) {
        editDialog = true;
        ProductDialogFragment dialogFragment = getProductDialogFragment(item, cache);
        return dialogFragment;
    }

    public static ProductDialogFragment newAddDialogInstance(ProductItem item, ProductActivityCache cache) {
        editDialog = false;
        ProductDialogFragment dialogFragment = getProductDialogFragment(item, cache);
        return dialogFragment;
    }

    private static ProductDialogFragment getProductDialogFragment(ProductItem item, ProductActivityCache cache) {
        ProductDialogFragment dialogFragment = new ProductDialogFragment();
        dialogFragment.setCache(cache);
        dialogFragment.setItem(item);
        return dialogFragment;
    }

    public void setCache(ProductActivityCache cache) {
        this.cache = cache;
    }

    public void setItem(ProductItem item) {
        this.item = item;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        opened = true; // flag to avoid opening this dialog twice
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        opened = false; // flag to avoid opening this dialog twice

        if (resetState && !saveConfirmed) {
            // if item was implicitly saved because of taking a picture for the product, then delete the product
            productService.deleteById(item.getId())
                    .doOnCompleted(() ->
                    {
                        ProductActivity productsActivity = (ProductActivity) cache.getActivity();
                        productsActivity.updateListView();
                        resetState = false;
                    });
        }
    }

    public static boolean isOpened() {
        return opened;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        InstanceFactoryAbstract instanceFactory = new InstanceFactory(cache.getActivity().getApplicationContext());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogColourful);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.product_dialog, null);

        dialogCache = new ProductDialogCache(v);

        dialogCache.getExpandableLayout().setVisibility(View.GONE);

        dialogCache.getProductName().setText(item.getProductName());
        dialogCache.getProductNotes().setText(item.getProductNotes());
        dialogCache.getQuantity().setText(item.getQuantity());
        dialogCache.getPrice().setText(item.getProductPrice());
        dialogCache.getCategory().setText(item.getProductCategory());
        dialogCache.getCustomStore().setText(item.getProductStore());
        dialogCache.getProductCheckBox().setChecked(item.isChecked());

        PackageManager pm = getContext().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            dialogCache.getCameraIcon().setVisibility(View.GONE);
            dialogCache.getProductImage().setVisibility(View.GONE);
        }

        if (editDialog) {
            dialogCache.getTitleTextView().setText(getActivity().getResources().getString(R.string.product_name_edit));
            dialogCache.getProductImage().setImageBitmap(item.getThumbnailBitmap());
            dialogCache.getProductCheckBox().setVisibility(View.VISIBLE);
        } else {
            dialogCache.getTitleTextView().setText(getActivity().getResources().getString(R.string.product_name_new));
            item.setDefaultImage(true);
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_menu_camera);
            dialogCache.getProductImage().setImageBitmap(bitmap);
            dialogCache.getProductCheckBox().setVisibility(View.GONE);
        }

        dialogCache.getButtonPlus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!LineUtil.isEmpty(String.valueOf(dialogCache.getQuantity().getText()))) {
                    int value = Integer.parseInt(String.valueOf(dialogCache.getQuantity().getText()));
                    value++;
                    String newQuantity = String.valueOf(value);
                    dialogCache.getQuantity().setText(newQuantity);
                } else {
                    dialogCache.getQuantity().setText(DEFAULT_QUANTITY);
                }
            }
        });

        dialogCache.getButtonMinus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LineUtil.isEmpty(String.valueOf(dialogCache.getQuantity().getText()))) {
                    int value = Integer.parseInt(String.valueOf(dialogCache.getQuantity().getText()));

                    if (value > 0) {
                        value--;
                        String newQuantity = String.valueOf(value);
                        dialogCache.getQuantity().setText(newQuantity);
                    }
                } else {
                    dialogCache.getQuantity().setText(DEFAULT_QUANTITY);
                }
            }
        });


        dialogCache.getExpandableImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogCache.getExpandableLayout().getVisibility() == View.GONE) {
                    changePhotoThumbnailVisibility(R.drawable.ic_keyboard_arrow_up_white_48sp, View.VISIBLE);
                } else {
                    changePhotoThumbnailVisibility(R.drawable.ic_keyboard_arrow_down_white_48sp, View.GONE);
                }
            }
        });


        dialogCache.getCameraIcon().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(cache.getActivity(), Manifest.permission.CAMERA);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    startImageCaptureAction();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                }

            }

        });

        dialogCache.getPrice().setFilters(new InputFilter[]{new PriceInputFilter(dialogCache)});


        dialogCache.getProductNotes().setOnFocusChangeListener(new ProductDialogFocusListener(dialogCache));
        dialogCache.getProductName().setOnFocusChangeListener(new ProductDialogFocusListener(dialogCache));

        Observable<AutoComplete> rxAutoCompleteLists = productService.getAutoCompleteListsObservable();
        AutoComplete autoCompleteLists = new AutoComplete();
        rxAutoCompleteLists
                .doOnNext(result -> result.copyTo(autoCompleteLists))
                .doOnCompleted(() -> setupAutoCompleteLists(autoCompleteLists))
                .subscribe();

        Set<String> productNames = new TreeSet<>();
        productService.getAllProducts(cache.getListId())
                .map(item -> item.getProductName())
                .doOnNext(name -> productNames.add(name))
                .subscribe();

        dialogCache.getQuantity().setOnFocusChangeListener(new ProductDialogFocusListener(dialogCache));
        dialogCache.getPrice().setOnFocusChangeListener(new ProductDialogFocusListener(dialogCache));

        dialogCache.getProductImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isDefaultImage() && !dialogCache.isImageScheduledForDeletion()) {
                    startPhotoPreviewActivity();
                }
            }
        });


        builder.setPositiveButton(cache.getActivity().getResources().getString(R.string.okay), new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton(cache.getActivity().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = String.valueOf(dialogCache.getProductName().getText());
                if (LineUtil.isEmpty(productName)) {
                    MessageUtil.showToast(getActivity().getApplicationContext(), R.string.alert_missing_product_name, Toast.LENGTH_LONG);
                } else {
                    saveConfirmed = true;
                    saveUserInput(productName);

                    productService.saveOrUpdate(item, cache.getListId())
                            .doOnCompleted(() ->
                            {
                                ProductActivity productsActivity = (ProductActivity) cache.getActivity();
                                productsActivity.updateListView();
                            })
                            .subscribe();
                    dialog.dismiss();
                }
            }
        });
        if (!editDialog) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        dialogCache.getProductNameInputLayout().setError(null);
        dialogCache.getProductName().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (productNames.contains(s.toString())) {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    dialogCache.getProductNameInputLayout().setError(getContext().getString(R.string.product_already_exists));
                } else {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                    dialogCache.getProductNameInputLayout().setError(null);
                }
            }
        });

        String format2Decimals = getResources().getString(R.string.number_format_2_decimals);
        String format1Decimal = getResources().getString(R.string.number_format_1_decimal);
        String format0Decimals = getResources().getString(R.string.number_format_0_decimals);
        dialogCache.getProductPriceInputLayout().setError(null);
        dialogCache.getPrice().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double number = LineUtil.getStringAsDouble(s.toString(), format2Decimals, format1Decimal, format0Decimals);
                if (number > MAX_PRICE_ALLOWED) {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    dialogCache.getProductPriceInputLayout().setError(getContext().getString(R.string.price_number_too_big));
                } else if (number == LineUtil.PARSE_ERROR && !s.toString().equals(LineUtil.EMPTY)) {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                    dialogCache.getProductPriceInputLayout().setError(getContext().getString(R.string.number_format_invalid));
                } else {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                    dialogCache.getProductPriceInputLayout().setError(null);
                }
            }
        });

        return dialog;
    }

    private void startPhotoPreviewActivity() {
        Intent viewPhotoIntent = new Intent(cache.getActivity(), PhotoPreviewActivity.class);
        viewPhotoIntent.putExtra(ProductActivity.PRODUCT_ID_KEY, item.getId());
        viewPhotoIntent.putExtra(ProductActivity.PRODUCT_NAME, item.getProductName());
        viewPhotoIntent.putExtra(ProductActivity.FROM_DIALOG, true);
        this.startActivityForResult(viewPhotoIntent, REQUEST_PHOTO_PREVIEW_FROM_DIALOG);
    }

    private void changePhotoThumbnailVisibility(int ic_keyboard_arrow_up_white_48sp, int visible) {
        dialogCache.getExpandableImageView().setImageResource(ic_keyboard_arrow_up_white_48sp);
        dialogCache.getExpandableLayout().setVisibility(visible);
    }

    private void saveUserInput(String productName) {
        item.setProductName(productName);
        item.setProductNotes(String.valueOf(dialogCache.getProductNotes().getText()));
        item.setQuantity(String.valueOf(dialogCache.getQuantity().getText()));
        item.setProductPrice(String.valueOf(dialogCache.getPrice().getText()));
        item.setProductCategory(String.valueOf(dialogCache.getCategory().getText()));
        item.setProductStore(String.valueOf(dialogCache.getCustomStore().getText()));
        item.setChecked(dialogCache.getProductCheckBox().isChecked());

        if (dialogCache.isImageScheduledForDeletion()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_menu_camera);
            File imageFile = new File(productService.getProductImagePath(item.getId()));
            imageFile.delete();
            item.setThumbnailBitmap(bitmap);
            item.setDefaultImage(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get(CameraActivity.THUMBNAIL_KEY);

            dialogCache.getProductImage().setImageBitmap(imageBitmap);
            item.setThumbnailBitmap(imageBitmap);
            item.setDefaultImage(false);
            changePhotoThumbnailVisibility(R.drawable.ic_keyboard_arrow_up_white_48sp, View.VISIBLE);
        } else if (requestCode == REQUEST_PHOTO_PREVIEW_FROM_DIALOG && resultCode == -1) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get(ProductActivity.PHOTO_BITMAP);

            dialogCache.getProductImage().setImageBitmap(imageBitmap);
            dialogCache.setImageScheduledForDeletion(true);
            changePhotoThumbnailVisibility(R.drawable.ic_keyboard_arrow_up_white_48sp, View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startImageCaptureAction();
                }
                return;
            }
        }
    }

    private void startImageCaptureAction() {
        // item must be saved first in order to have an id.
        // id is needed to generate a unique file name for the image
        String productName = String.valueOf(dialogCache.getProductName().getText());
        if (LineUtil.isEmpty(productName)) {
            String newProductName = getResources().getString(R.string.new_product);
            productName = newProductName;
        }
        boolean newProductAdded = item.getId() == null;
        resetState = true && newProductAdded;
        saveUserInput(productName);
        productService.saveOrUpdate(item, cache.getListId()).subscribe();
        saveConfirmed = false;

        dialogCache.setImageScheduledForDeletion(false);
        Intent takePictureIntent = new Intent(cache.getActivity(), CameraActivity.class);
        takePictureIntent.putExtra(ProductActivity.PRODUCT_ID_KEY, item.getId());
        takePictureIntent.putExtra(ProductActivity.PRODUCT_NAME, item.getProductName());
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void setupAutoCompleteLists(AutoComplete autoCompleteLists) {
        String[] productsArray = autoCompleteLists.getProductsArray();
        ArrayAdapter<String> productNamesAdapter = new ArrayAdapter<>(getActivity(), R.layout.pfa_lists, productsArray);
        dialogCache.getProductName().setAdapter(productNamesAdapter);

        String[] storesArray = autoCompleteLists.getStoresArray();
        ArrayAdapter<String> storesAdapter = new ArrayAdapter<>(getActivity(), R.layout.pfa_lists, storesArray);
        dialogCache.getCustomStore().setAdapter(storesAdapter);

        String[] categoryArray = autoCompleteLists.getCategoryArray();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.pfa_lists, categoryArray);
        dialogCache.getCategory().setAdapter(categoryAdapter);
    }
}
