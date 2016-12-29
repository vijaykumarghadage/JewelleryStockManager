package com.vj.jewellerystockmanager.itemdetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ItemList class
 * Created by vijaykumargh on 08/08/2016.
 */
public class ItemList implements Serializable {

    private ArrayList<ItemBean> mItemList;


    public ArrayList<ItemBean> getItemList() {
        return mItemList;
    }

    public void setItemList(ArrayList<ItemBean> itemList) {
        this.mItemList = itemList;
    }

    @Override
    public String toString() {
        return "ItemList{" +
                "mItemList=" + mItemList +
                '}';
    }
}
