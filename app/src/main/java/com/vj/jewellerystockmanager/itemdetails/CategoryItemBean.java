package com.vj.jewellerystockmanager.itemdetails;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Bean for Parent of Item
 * Created by vijaykumargh on 12/08/2016.
 */
public class CategoryItemBean implements Serializable {

    private String mName;
    private String mImageUrl;
    private HashMap<String, ItemBean> mItemList;

    public CategoryItemBean() {
    }

    public CategoryItemBean(String mName, String mImageUrl, HashMap<String, ItemBean> itemList) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mItemList = itemList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public HashMap<String, ItemBean> getItemList() {
        return mItemList;
    }

    public void setItemList(HashMap<String, ItemBean> itemList) {
        this.mItemList = itemList;
    }

    @Override
    public String toString() {
        return "CategoryItemBean{" +
                "mName='" + mName + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mItemList=" + mItemList +
                '}';
    }
}
