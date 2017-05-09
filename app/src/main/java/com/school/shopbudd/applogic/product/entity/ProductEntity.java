package com.school.shopbudd.applogic.product.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.school.shopbudd.api.database.EntityAbstract;
import com.school.shopbudd.applogic.shoppinglists.entity.ShoppingListEntity;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * @author NightPlex
 */
public class ProductEntity extends EntityAbstract {
    @DatabaseField
    private String productName;

    @DatabaseField
    private Integer quantity;

    @DatabaseField
    private String notes;

    @DatabaseField
    private String store;

    @DatabaseField
    private Double price;

    @DatabaseField
    private Integer icon;

    @DatabaseField
    private Boolean selected;

    @DatabaseField
    private String category;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] imageBytes;

    @DatabaseField(foreign = true, canBeNull = false)
    private ShoppingListEntity shoppingList;

    public ProductEntity() {
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public ShoppingListEntity getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingListEntity shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public String toString() {
        return "ProductItemEntity{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", notes='" + notes + '\'' +
                ", store='" + store + '\'' +
                ", price=" + price +
                ", icon=" + icon +
                ", selected=" + selected +
                ", category='" + category + '\'' +
                ", shoppingList=" + shoppingList +
                '}';
    }
}
