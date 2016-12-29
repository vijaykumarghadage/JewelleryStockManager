package com.vj.jewellerystockmanager.home;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.itemdetails.CategoryItemBean;
import com.vj.jewellerystockmanager.itemdetails.ItemBean;

import java.util.HashMap;

/**
 * Class acts as popup for showing add item dialog
 * Created by vijaykumargh on 10/08/2016.
 */
public class AddItemDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "AddItemDialog";
    private EditText etItemId;
    private EditText etItemName;
    private EditText etItemQty;
    private EditText etItemWt;
    private EditText etItemPurity;

    private boolean mIsUpdate;
    private String mStockType_category;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_item_data, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mIsUpdate = getArguments().getBoolean(Constants.IS_UPDATE_ITEM);
        mStockType_category = getArguments().getString(Constants.STOCK_TYPE_CATEGORY, null);

        etItemId = (EditText) view.findViewById(R.id.etItemId);
        etItemName = (EditText) view.findViewById(R.id.etItemName);
        etItemQty = (EditText) view.findViewById(R.id.etItemQty);
        etItemWt = (EditText) view.findViewById(R.id.etItemWt);
        etItemPurity = (EditText) view.findViewById(R.id.etItemPurity);

        Button btnAdd = (Button) view.findViewById(R.id.btnAddOrUpdate);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);

        if (mIsUpdate) {
            ItemBean item = (ItemBean) getArguments().getSerializable(Constants.ITEM_BEAN);
            etItemId.setText(item.getItemId().split("_")[2].substring(1));
            etItemName.setText(item.getItemName());
            etItemQty.setText(String.valueOf(item.getQuantity()));
            etItemWt.setText(String.valueOf(item.getWeight()));
            etItemPurity.setText(String.valueOf(item.getPurity()));
            etItemId.setEnabled(false);
            etItemWt.setEnabled(false);

            btnAdd.setText(getString(R.string.update));
        }

        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddOrUpdate:
                updateDatabase();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * Method used to update database
     */
    private void updateDatabase() {

        String itemId = etItemId.getText().toString().trim();
        String itemName = etItemName.getText().toString().trim();
        String qty = etItemQty.getText().toString().trim();
        String wt = etItemWt.getText().toString().trim();
        String purity = etItemPurity.getText().toString().trim();
        if (isDataValid(itemId, itemName, qty, wt, purity)) {

            HashMap<String, HashMap<String, CategoryItemBean>> stockTypeListMap =
                    JSMApplication.getStockTypeListMap();
            String path[] = mStockType_category.split("_");
            Log.d(TAG, "Stock_Category: " + mStockType_category);
            String stockType = path[0];
            String categoryKey = path[1];
            HashMap<String, CategoryItemBean> categoryListMap = stockTypeListMap.get(stockType);
            CategoryItemBean category = categoryListMap.get(categoryKey);
            HashMap<String, ItemBean> iListMap = category.getItemList();

            itemId = "i" + itemId;
            if (mIsUpdate) {
                ItemBean item = new ItemBean();
                //item.setItemId(itemId);
                item.setItemId(stockType + "_" + categoryKey + "_" + itemId);
                item.setItemName(itemName);
                item.setQuantity(Integer.parseInt(qty));
                item.setWeight(Double.parseDouble(wt));
                item.setPurity(Double.parseDouble(purity));

                iListMap.put(itemId, item);
                JSMApplication.setStockTypeListMap(stockTypeListMap);
                DatabaseReference itemRef = JSMApplication.getDBItemsReference().child(stockType)
                        .child(categoryKey).child("itemList").child(itemId);
                itemRef.setValue(item);

                Toast.makeText(getActivity(), R.string.item_updated_successfully, Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                if (!iListMap.containsKey(itemId)) {
                    ItemBean item = new ItemBean();
                    item.setItemId(stockType + "_" + categoryKey + "_" + itemId);
                    item.setItemName(itemName);
                    item.setQuantity(Integer.parseInt(qty));
                    item.setWeight(Double.parseDouble(wt));
                    item.setPurity(Double.parseDouble(purity));

                    iListMap.put(itemId, item);
                    JSMApplication.setStockTypeListMap(stockTypeListMap);

                    DatabaseReference itemListRef = JSMApplication.getDBItemsReference().child(stockType)
                            .child(categoryKey).child("itemList");
                    itemListRef.setValue(iListMap);

                    Toast.makeText(getActivity(), R.string.item_added_successfully, Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    showAlert();
                }
            }
        }
    }


    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle(R.string.alert);
        alertDialogBuilder
                .setMessage(R.string.item_id_already_exists)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /**
     * Method used to check validity of entered data
     *
     * @param itemId   id
     * @param itemName name
     * @param qty      quantity
     * @param wt       weight
     * @param purity   purity
     * @return true if all fields are valid
     */
    private boolean isDataValid(String itemId, String itemName, String qty, String wt, String purity) {

        if (itemId.isEmpty()) {
            etItemId.setError("Please enter valid Item Id.");
        } else if (itemName.isEmpty()) {
            etItemName.setError("Please enter valid Item Name.");
        } else if (qty.isEmpty() || Long.parseLong(qty) < 1) {
            etItemQty.setError("Please enter valid Item Quantity.");
        } else if (wt.isEmpty() || Double.parseDouble(wt) <= 0) {
            etItemWt.setError("Please enter valid Item Weight.");
        } else if (purity.isEmpty()) {
            etItemPurity.setError("Please enter valid Item Purity.");
        } else {
            return true;
        }
        return false;
    }
}
