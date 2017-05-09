package com.school.shopbudd.applogic.product.model;

import android.content.Context;
import android.content.ContextWrapper;
import com.school.shopbudd.R;
import com.school.shopbudd.api.database.Database;
import com.school.shopbudd.applogic.product.ProductService;
import com.school.shopbudd.applogic.product.converter.ProductConvertorService;
import com.school.shopbudd.applogic.product.entity.ProductEntity;
import com.school.shopbudd.applogic.product.entity.ProductItemDAO;
import com.school.shopbudd.applogic.shoppinglists.ShoppingListService;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListEntity;
import com.school.shopbudd.applogic.shoppinglists.model.ListItem;
import com.school.shopbudd.utils.LineUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class DefaultProductService implements ProductService {
    private static final String IMAGE_DIR_NAME = "imageDir";
    private static final String EXTENSION = ".jpg";

    private ProductItemDAO productItemDao;
    private ProductConvertorService converterService;
    private ShoppingListService shoppingListService;
    private Context context;

    @Inject
    public DefaultProductService(
            ProductItemDAO productItemDao,
            ProductConvertorService converterService,
            ShoppingListService shoppingListService
    ) {
        this.productItemDao = productItemDao;
        this.converterService = converterService;
        this.shoppingListService = shoppingListService;
    }

    @Override
    public void setContext(Context context, Database db) {
        this.context = context;
        productItemDao.setContext(context, db);
        shoppingListService.setContext(context, db);
        converterService.setContext(context, db);
    }

    @Override
    public Observable<Void> saveOrUpdate(ProductItem item, String listId) {
        Observable<Void> observable = Observable
                .fromCallable(() -> saveOrUpdateSync(item, listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void saveOrUpdateSync(ProductItem item, String listId) {
        ProductEntity entity = new ProductEntity();
        converterService.convertItemToEntity(item, entity);

        ShoppingListEntity shoppingListEntity = shoppingListService.getEntityByIdSync(listId);
        entity.setShoppingList(shoppingListEntity);

        productItemDao.save(entity);
        item.setId(entity.getId().toString());
        return null;
    }

    @Override
    public Observable<Void> duplicateProducts(String listId) {
        Observable<Void> observable = Observable
                .fromCallable(() -> duplicateProductsSync(listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void duplicateProductsSync(String listId) {
        ListItem newList = shoppingListService.getById(listId)
                .doOnNext(listItem ->
                {
                    listItem.setId(null); // new list
                    listItem.setListName(getNewName(listItem));
                })
                .toBlocking().single();

        shoppingListService.saveOrUpdateSync(newList);

        List<ProductItem> products = getAllProductsSync(listId);
        for (ProductItem product : products) {
            copyToListSync(product, newList.getId());
        }
        return null;
    }

    @Override
    public Observable<Void> copyToList(ProductItem product, String listId) {
        Observable<Void> observable = Observable
                .fromCallable(() -> copyToListSync(product, listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void copyToListSync(ProductItem product, String listId) {
        String originalProductId = product.getId();
        product.setId(null); // new product
        product.setChecked(false);
        saveOrUpdateSync(product, listId);
        String newProductId = product.getId();
        copyImage(originalProductId, newProductId);
        return null;
    }

    private void copyImage(String sourceProductId, String destProductId) {
        File srcImage = new File(getProductImagePath(sourceProductId));
        if (srcImage.exists()) {
            try {
                File destFile = new File(getProductImagePath(destProductId));
                copy(srcImage, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Observable<Void> resetCheckedProducts(String listId) {
        Observable<Void> observable = Observable
                .fromCallable(() -> resetCheckedProductsSync(listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void resetCheckedProductsSync(String listId) {
        List<ProductItem> products = getAllProductsSync(listId);
        for (ProductItem product : products) {
            product.setChecked(false);
            saveOrUpdateSync(product, listId);
        }
        return null;
    }

    private String getNewName(ListItem listItem) {
        return listItem.getListName() + LineUtil.SPACE + context.getResources().getString(R.string.duplicated_suffix);
    }

    @Override
    public Observable<ProductItem> getById(String entityId) {
        Observable<ProductItem> observable = Observable
                .fromCallable(() -> getByIdSync(entityId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private ProductItem getByIdSync(String entityId) {
        ProductEntity productEntity = productItemDao.getById(Long.valueOf(entityId));
        if (productEntity == null) return null;

        ProductItem item = new ProductItem();
        converterService.convertEntitiesToItem(productEntity, item);
        return item;
    }

    @Override
    public String getProductImagePath(String id) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir(IMAGE_DIR_NAME, Context.MODE_PRIVATE);
        File path = new File(directory, getUniqueName(id));
        return path.getAbsolutePath();
    }

    @Override
    public Observable<Void> deleteById(String id) {
        Observable<Void> observable = Observable
                .fromCallable(() -> deleteByIdSync(id))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void deleteByIdSync(String id) {
        productItemDao.deleteById(Long.valueOf(id));

        // delete imageFile if exists
        File imageFile = new File(getProductImagePath(id));
        imageFile.delete();
        return null;
    }

    @Override
    public Observable<Void> deleteOnlyImage(String id) {
        Observable observable = Observable
                .defer(() -> Observable.just(deleteOnlyImageSync(id)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void deleteOnlyImageSync(String id) {
        ProductEntity entity = productItemDao.getById(Long.valueOf(id));
        entity.setImageBytes(null);
        productItemDao.save(entity);

        File imageFile = new File(getProductImagePath(id));
        imageFile.delete();
        return null;
    }

    @Override
    public Observable<Void> deleteSelected(List<ProductItem> productItems) {
        Observable<Void> observable = Observable
                .fromCallable(() -> deleteSelectedSync(productItems))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void deleteSelectedSync(List<ProductItem> productItems) {
        Observable.from(productItems)
                .filter(item -> item.isSelectedForDeletion())
                .subscribe(item -> deleteByIdSync(item.getId()));
        return null;
    }

    @Override
    public Observable<ProductItem> getAllProducts(String listId) {
        Observable<ProductItem> observable = Observable
                .defer(() -> Observable.from(getAllProductsSync(listId)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private List<ProductItem> getAllProductsSync(String listId) {
        List<ProductItem> productItems = new ArrayList<>();

        Observable
                .from(productItemDao.getAllEntities())
                .filter(entity -> entity.getShoppingList().getId() == Long.valueOf(listId))
                .map(this::getItem)
                .subscribe(item -> productItems.add(item));

        return productItems;
    }

    @Override
    public Observable<ProductItem> getAllProducts() {
        Observable<ProductItem> observable = Observable
                .defer(() -> Observable.from(getAllProductsSync()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private List<ProductItem> getAllProductsSync() {
        List<ProductItem> productDtos = new ArrayList<>();

        Observable
                .from(productItemDao.getAllEntities())
                .map(this::getItem)
                .subscribe(item -> productDtos.add(item));

        return productDtos;
    }

    @Override
    public Observable<TotalItem> getInfo(String listId) {
        Observable<TotalItem> observable = Observable
                .fromCallable(() -> getInfoSync(listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private TotalItem getInfoSync(String listId) {
        List<ProductItem> productItems = getAllProductsSync(listId);
        TotalItem totalItem = computeTotals(productItems);
        return totalItem;
    }

    @Override
    public Observable<Void> deleteAllFromList(String listId) {
        Observable<Void> observable = Observable
                .fromCallable(() -> deleteAllFromListSync(listId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    private Void deleteAllFromListSync(String listId) {
        List<ProductItem> productItems = getAllProductsSync(listId);
        Observable.from(productItems)
                .subscribe(item -> deleteByIdSync(item.getId()));
        return null;
    }

    @Override
    public List<ProductItem> moveSelectedToEnd(List<ProductItem> productItems) {
        List<ProductItem> nonSelectedItems = new ArrayList<>();
        Observable
                .from(productItems)
                .filter(item -> !item.isChecked())
                .subscribe(item -> nonSelectedItems.add(item));

        List<ProductItem> selectedItems = new ArrayList<>();
        Observable
                .from(productItems)
                .filter(item -> item.isChecked())
                .subscribe(item -> selectedItems.add(item));

        nonSelectedItems.addAll(selectedItems);
        productItems = nonSelectedItems;
        return productItems;
    }

    @Override
    public TotalItem computeTotals(List<ProductItem> productItems) {
        double totalAmount = 0.0;
        double checkedAmount = 0.0;
        int nrProducts = 0;
        for (ProductItem item : productItems) {
            Integer quantity = Integer.valueOf(item.getQuantity());
            nrProducts += quantity;
            String price = item.getProductPrice();
            if (price != null) {
                double priceAsDouble = converterService.getStringAsDouble(price) * quantity;
                totalAmount += priceAsDouble;
                if (item.isChecked()) {
                    checkedAmount += priceAsDouble;
                }
            }
        }

        TotalItem totalItem = new TotalItem();

        if (totalAmount == 0.0) {
            totalItem.setEqualsZero(true);
        }

        String totalAmountString = converterService.getDoubleAsString(totalAmount);
        String checkedAmountString = converterService.getDoubleAsString(checkedAmount);
        totalItem.setNrProducts(nrProducts);

        return totalItem;
    }


    @Override
    public Boolean isSearched(String[] searchedTexts, ProductItem item) {
        String name = item.getProductName().toLowerCase();
        String category = item.getProductCategory().toLowerCase();
        String store = item.getProductStore().toLowerCase();
        String notes = item.getProductNotes().toLowerCase();
        String searchableText = name + LineUtil.SPACE + category + LineUtil.SPACE + store + LineUtil.SPACE + notes;
        for (String searchedText : searchedTexts) {
            if (searchableText.contains(searchedText.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSharableText(ProductItem item) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(LineUtil.DASH)
                .append(LineUtil.LEFT_BRACE)
                .append(item.getQuantity())
                .append(LineUtil.RIGHT_BRACE)
                .append(item.getProductName());

        if (!LineUtil.isEmpty(item.getProductNotes())) {
            sb
                    .append(LineUtil.NEW_LINE)
                    .append(LineUtil.NEW_LINE)
                    .append(item.getProductNotes());
        }

        return sb.toString();
    }

    @Override
    public Observable<AutoComplete> getAutoCompleteListsObservable() {
        Observable autoCompleteListsObservable = Observable
                .defer(() -> Observable.just(getAutoCompleteLists()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
        return autoCompleteListsObservable;
    }

    private AutoComplete getAutoCompleteLists() {
        AutoComplete autoCompleteLists = new AutoComplete();

        Observable
                .from(productItemDao.getAllEntities())
                .map(this::getItem)
                .subscribe(item -> autoCompleteLists.updateLists(item));
        return autoCompleteLists;
    }

    @Override
    public Observable<Boolean> deleteInvisibleProductsFromDb(List<String> listIds) {
        Observable<Boolean> observable = Observable.from(productItemDao.getAllEntities())
                .filter(entity -> !listIds.contains(String.valueOf(entity.getShoppingList().getId())))
                .map(entity -> productItemDao.deleteById(entity.getId()))
                .subscribeOn(Schedulers.computation());
        return observable;
    }


    private ProductItem getItem(ProductEntity entity) {
        ProductItem item = new ProductItem();
        converterService.convertEntitiesToItem(entity, item);
        return item;
    }

    private String getUniqueName(String productId) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(productId)
                .append(EXTENSION);
        return sb.toString();
    }

    private void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
