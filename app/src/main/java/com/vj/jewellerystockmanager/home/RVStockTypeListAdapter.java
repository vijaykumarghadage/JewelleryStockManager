package com.vj.jewellerystockmanager.home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.itemdetails.CategoryDetailsActivity;
import com.vj.jewellerystockmanager.itemdetails.CategoryItemBean;
import com.vj.jewellerystockmanager.itemdetails.ItemBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter class for stock type list
 * Created by vijaykumargh on 12/08/2016.
 */
public class RVStockTypeListAdapter extends RecyclerView.Adapter<RVStockTypeListAdapter.StockTypeListHolder> implements View.OnClickListener {
    private ArrayList<String> mTypeList;
    private Activity mActivity;

    public class StockTypeListHolder extends RecyclerView.ViewHolder {

        public TextView tvRowStockTypeName;
        public TextView tvRowCategoryCount;
        public TextView tvRowTotalItems;
        public ImageButton ibCatMenu;

        public StockTypeListHolder(View view) {
            super(view);
            view.setTag(this);
            tvRowStockTypeName = (TextView) view.findViewById(R.id.tvRowStockTypeName);
            tvRowCategoryCount = (TextView) view.findViewById(R.id.tvRowCategoryCount);
            tvRowTotalItems = (TextView) view.findViewById(R.id.tvRowTotalItems);
            ibCatMenu = (ImageButton) view.findViewById(R.id.ibCatMenu);
        }
    }

    public RVStockTypeListAdapter(Activity activity, ArrayList<String> typeList) {
        mActivity = activity;
        mTypeList = typeList;
    }

    @Override
    public StockTypeListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_stock_type_row, parent, false);
        view.setOnClickListener(this);
        return new StockTypeListHolder(view);
    }

    @Override
    public void onBindViewHolder(StockTypeListHolder holder, int position) {
        String key = mTypeList.get(position);
        holder.tvRowStockTypeName.setText(key);
        HashMap<String, HashMap<String, CategoryItemBean>> tempMap = JSMApplication.getStockTypeListMap();
        HashMap<String, CategoryItemBean> categoryMap = tempMap.get(key);
        holder.tvRowCategoryCount.setText(String.valueOf(categoryMap.size()));
        int itemsCount = 0;
        for (String catKey : categoryMap.keySet()) {
            HashMap<String, ItemBean> list = categoryMap.get(catKey).getItemList();
            for (String iKey : list.keySet()) {
                itemsCount += list.get(iKey).getQuantity();
            }
        }
        holder.tvRowTotalItems.setText(String.valueOf(itemsCount));
        holder.ibCatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mActivity, view);
                popupMenu.getMenuInflater().inflate(R.menu.stock_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addCategoryItem:
                                // ((BaseActivity) mActivity).showAddItemAlertDialog(false, mStockType + "_" + categoryKey, null);
                                break;
                            case R.id.removeStock:
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    @Override
    public void onClick(View view) {
        StockTypeListHolder holder = (StockTypeListHolder) view.getTag();
        String stockType = mTypeList.get(holder.getAdapterPosition());
        Intent intent = new Intent(mActivity, CategoryDetailsActivity.class);
        intent.putExtra(Constants.STOCK_TYPE, stockType);
        mActivity.startActivity(intent);
    }
}
