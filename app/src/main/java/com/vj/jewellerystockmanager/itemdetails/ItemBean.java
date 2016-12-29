package com.vj.jewellerystockmanager.itemdetails;

import java.io.Serializable;

/**
 * Model class for Item
 * Created by vijaykumargh on 08/08/2016.
 */
public class ItemBean implements Serializable {

    private String mItemId;
    private String mItemName;
    private long mQuantity;
    private double mWeight;
    private double mPurity;
    private String mImageUrl;

    public ItemBean() {
    }

    public ItemBean(String mItemId, String mItemName, long mQuantity, double mWeight, double mPurity, String mImageUrl) {
        this.mItemId = mItemId;
        this.mItemName = mItemName;
        this.mQuantity = mQuantity;
        this.mWeight = mWeight;
        this.mPurity = mPurity;
        this.mImageUrl = mImageUrl;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        this.mItemId = itemId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        this.mItemName = itemName;
    }

    public long getQuantity() {
        return mQuantity;
    }

    public void setQuantity(long quantity) {
        this.mQuantity = quantity;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        this.mWeight = weight;
    }

    public double getPurity() {
        return mPurity;
    }

    public void setPurity(double purity) {
        this.mPurity = purity;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "mItemId=" + mItemId +
                ", mItemName='" + mItemName + '\'' +
                ", mQuantity=" + mQuantity +
                ", mWeight=" + mWeight +
                ", mPurity=" + mPurity +
                ", mImageUrl='" + mImageUrl + '\'' +
                '}';
    }
}
