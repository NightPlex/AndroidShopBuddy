package com.school.shopbudd.applogic.shoppinglists.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.school.shopbudd.api.database.EntityAbstract;
import com.school.shopbudd.applogic.product.entity.ProductEntity;

import java.util.Date;

/**
 * Created by NightPlex
 * Github: https://github.com/NightPlex
 *
 * Using orm lite for android simple list entity.
 *
 * @author NightPlex
 */
public class ShoppingListEntity extends EntityAbstract {
    @DatabaseField(canBeNull = false)
    private String listName;

    @DatabaseField
    private String priority;

    @DatabaseField
    private Integer icon;

    @DatabaseField
    private Date deadline;

    @DatabaseField
    private String notes;

    @DatabaseField
    private String sortCriteria;

    @DatabaseField
    private boolean sortAscending;

    @DatabaseField
    private Integer reminderCount;

    @DatabaseField
    private Integer reminderUnit; // index of spinner

    @DatabaseField
    private boolean statisticsEnabled;

    @ForeignCollectionField
    private ForeignCollection<ProductEntity> products;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ForeignCollection<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(ForeignCollection<ProductEntity> products) {
        this.products = products;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public boolean getSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public Integer getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(Integer reminderCount) {
        this.reminderCount = reminderCount;
    }

    public Integer getReminderUnit() {
        return reminderUnit;
    }

    public void setReminderUnit(Integer reminderUnit) {
        this.reminderUnit = reminderUnit;
    }

    public boolean getStatisticsEnabled() {
        return statisticsEnabled;
    }

    public void setStatisticsEnabled(boolean statisticsEnabled) {
        this.statisticsEnabled = statisticsEnabled;
    }

    @Override
    public String toString() {
        return "ShoppingListEntity{" +
                "listName='" + listName + '\'' +
                ", priority='" + priority + '\'' +
                ", icon=" + icon +
                ", deadline=" + deadline +
                ", notes='" + notes + '\'' +
                ", sortCriteria='" + sortCriteria + '\'' +
                ", sortAscending=" + sortAscending +
                ", reminderCount=" + reminderCount +
                ", reminderUnit=" + reminderUnit +
                ", statisticsEnabled=" + statisticsEnabled +
                ", products=" + products +
                '}';
    }
}
