package com.vj.jewellerystockmanager.framework.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.home.AddItemDialog;
import com.vj.jewellerystockmanager.itemdetails.ItemBean;

import java.util.Comparator;

/**
 * Parent activity of all other Activities
 * Created by vijaykumargh on 08/08/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Method used to launch add item alert dialog
     *
     * @param isUpdate : update status
     * @param item     : item
     */
    public void showAddItemAlertDialog(boolean isUpdate, String stockType_category, ItemBean item) {
        AddItemDialog dialog = new AddItemDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_UPDATE_ITEM, isUpdate);
        bundle.putString(Constants.STOCK_TYPE_CATEGORY, stockType_category);
        bundle.putSerializable(Constants.ITEM_BEAN, item);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "AddItemAlertDialog");
    }


    public class ItemIdComparator implements Comparator<ItemBean> {
        @Override
        public int compare(ItemBean ib1, ItemBean ib2) {
            return ib1.getItemId().compareTo(ib2.getItemId());
        }
    }
}
