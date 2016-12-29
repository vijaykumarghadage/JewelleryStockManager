package com.vj.jewellerystockmanager.itemdetails;

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
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.framework.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter for category details
 * Created by vijaykumargh on 16/08/2016.
 */
public class RVCategoryListAdapter extends RecyclerView.Adapter<RVCategoryListAdapter.VHCategoryListHolder> implements View.OnClickListener {

    private ArrayList<String> mCategoryList;
    private HashMap<String, CategoryItemBean> mCategoryListMap;
    private Activity mActivity;
    private String mStockType;

    public class VHCategoryListHolder extends RecyclerView.ViewHolder {

        public TextView tvCategoryName;
        public TextView tvCategoryTotalQty;
        public TextView tvCategoryTotalWt;
        public ImageButton ibCategoryMenu;

        public VHCategoryListHolder(View view) {
            super(view);
            view.setTag(this);
            tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
            tvCategoryTotalQty = (TextView) view.findViewById(R.id.tvCategoryTotalQty);
            tvCategoryTotalWt = (TextView) view.findViewById(R.id.tvCategoryTotalWt);
            ibCategoryMenu = (ImageButton) view.findViewById(R.id.ibCategoryMenu);
        }

    }

    public RVCategoryListAdapter(Activity activity, ArrayList<String> categoryList) {
        mActivity = activity;
        mCategoryList = categoryList;
    }

    @Override
    public VHCategoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_category_row, parent, false);
        view.setOnClickListener(this);
        return new VHCategoryListHolder(view);
    }

    @Override
    public void onBindViewHolder(VHCategoryListHolder holder, int position) {
        final String categoryKey = mCategoryList.get(position);
        CategoryItemBean category = mCategoryListMap.get(categoryKey);
        holder.tvCategoryName.setText(category.getName());

        int itemsCount = 0;
        double totalWt = 0;
        for (String key : category.getItemList().keySet()) {
            ItemBean item = category.getItemList().get(key);
            itemsCount += item.getQuantity();
            totalWt += item.getWeight();
        }
        holder.tvCategoryTotalQty.setText(String.valueOf(itemsCount));
        holder.tvCategoryTotalWt.setText(String.valueOf(totalWt));
        holder.ibCategoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mActivity, view);
                popupMenu.getMenuInflater().inflate(R.menu.category_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addItem:
                                ((BaseActivity) mActivity).showAddItemAlertDialog(false, mStockType + "_" + categoryKey, null);
                                break;
                            case R.id.removeCategory:
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
        return mCategoryList.size();
    }

    /**
     * updates map
     *
     * @param categoryListMap updated map
     */
    public void setCategoryListMap(HashMap<String, CategoryItemBean> categoryListMap) {
        mCategoryListMap = categoryListMap;
    }

    public void setStockType(String stockType) {
        this.mStockType = stockType;
    }

    @Override
    public void onClick(View view) {
        VHCategoryListHolder holder = (VHCategoryListHolder) view.getTag();
        String categoryKey = mCategoryList.get(holder.getAdapterPosition());
        CategoryItemBean category = mCategoryListMap.get(categoryKey);
        Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
        intent.putExtra(Constants.CATEGORY_KEY, categoryKey);
        intent.putExtra(Constants.STOCK_TYPE, mStockType);
        intent.putExtra(Constants.CATEGORY_BEAN, category);
        mActivity.startActivity(intent);
    }

}
