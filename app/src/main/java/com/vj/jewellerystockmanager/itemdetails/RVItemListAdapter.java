package com.vj.jewellerystockmanager.itemdetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter class for item list
 * Created by vijaykumargh on 09/08/2016.
 */
public class RVItemListAdapter extends RecyclerView.Adapter<RVItemListAdapter.VHItemListHolder> {

    private ArrayList<String> mItemList;
    private Activity mActivity;
    private HashMap<String, ItemBean> mItemListMap;
    private String mStockType;

    public class VHItemListHolder extends RecyclerView.ViewHolder {

        public TextView tvRowItemId;
        public TextView tvRowItemName;
        public TextView tvRowItemQty;
        public TextView tvRowItemWt;
        public TextView tvRowItemPurity;
        public ImageButton ibItemListMenu;

        public VHItemListHolder(View view) {
            super(view);
            tvRowItemId = (TextView) view.findViewById(R.id.tvRowItemId);
            tvRowItemName = (TextView) view.findViewById(R.id.tvRowItemName);
            tvRowItemQty = (TextView) view.findViewById(R.id.tvRowItemQty);
            tvRowItemWt = (TextView) view.findViewById(R.id.tvRowItemWt);
            tvRowItemPurity = (TextView) view.findViewById(R.id.tvRowItemPurity);
            ibItemListMenu = (ImageButton) view.findViewById(R.id.ibItemListMenu);
        }
    }

    public RVItemListAdapter(Activity activity, ArrayList<String> itemList) {
        mActivity = activity;
        mItemList = itemList;
    }

    @Override
    public VHItemListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_row, parent, false);
        return new VHItemListHolder(view);
    }

    @Override
    public void onBindViewHolder(final VHItemListHolder holder, int position) {

        String itemKey = mItemList.get(position);
        final ItemBean itemBean = mItemListMap.get(itemKey);
        final String categoryKey = itemBean.getItemId().split("_")[1];
        String itemId = itemKey.substring(1);
        holder.tvRowItemId.setText(itemId);
        holder.tvRowItemName.setText(itemBean.getItemName());
        holder.tvRowItemQty.setText(String.valueOf(itemBean.getQuantity()));
        holder.tvRowItemWt.setText(String.valueOf(itemBean.getWeight()));
        holder.tvRowItemPurity.setText(String.valueOf(itemBean.getPurity()));
        holder.ibItemListMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mActivity, view);
                popupMenu.getMenuInflater().inflate(R.menu.item_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.updateItem:
                                ((BaseActivity) mActivity).showAddItemAlertDialog(true, mStockType + "_" + categoryKey, itemBean);
                                break;
                            case R.id.removeItem:
                                removeQtys(itemBean, false);
                                break;
                            case R.id.removeAllItem:
                                removeQtys(itemBean, true);
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
        return mItemList.size();
    }


    public void setItemListMap(HashMap<String, ItemBean> itemListMap) {
        this.mItemListMap = itemListMap;
    }

    public void setStockType(String stockType) {
        this.mStockType = stockType;
    }

    /**
     * Method used to remove item(one quantity or all)
     *
     * @param itemBean item
     * @param isAll    true/false
     */
    private void removeQtys(final ItemBean itemBean, final boolean isAll) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        // set title
        alertDialogBuilder.setTitle(R.string.alert);

        String msg = (isAll) ? mActivity.getString(R.string.remove_all_items)
                : mActivity.getString(R.string.remove_single_items);
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String originalItemId = itemBean.getItemId();
                        String[] idPath = originalItemId.split("_");
                        String stockType = idPath[0];
                        String categoryKey = idPath[1];
                        String itemIdKey = idPath[2];

                        HashMap<String, HashMap<String, CategoryItemBean>> stockTypeListMap =
                                JSMApplication.getStockTypeListMap();
                        HashMap<String, CategoryItemBean> categoryListMap = stockTypeListMap.get(stockType);
                        CategoryItemBean category = categoryListMap.get(categoryKey);
                        HashMap<String, ItemBean> iListMap = category.getItemList();
                        DatabaseReference itemListRef = JSMApplication.getDBItemsReference().child(stockType)
                                .child(categoryKey).child("itemList");
                        if (isAll) {
                            iListMap.remove(itemIdKey);
                            itemListRef.setValue(iListMap);
                        } else {
                            long qty = itemBean.getQuantity();
                            if (qty > 1) {
                                itemBean.setQuantity(qty - 1);
                                iListMap.put(itemIdKey, itemBean);
                                itemListRef.child(itemIdKey).setValue(itemBean);
                            } else {
                                iListMap.remove(itemIdKey);
                                itemListRef.setValue(iListMap);
                            }
                        }
                        JSMApplication.setStockTypeListMap(stockTypeListMap);
                        if (isAll) {
                            Toast.makeText(mActivity, R.string.item_removed_successfully, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, R.string.one_qty_removed_successfully, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}
