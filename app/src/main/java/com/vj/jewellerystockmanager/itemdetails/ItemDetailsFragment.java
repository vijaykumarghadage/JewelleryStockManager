package com.vj.jewellerystockmanager.itemdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.framework.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Fragment for item details
 * Created by vijaykumargh on 16/08/2016.
 */
public class ItemDetailsFragment extends BaseFragment {

    private static final String TAG = "ItemDetailsFragment";
    private ItemDetailsActivity mActivity;
    private ArrayList<String> mItemList = new ArrayList<>();
    private RVItemListAdapter mRVAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mActivity = (ItemDetailsActivity) getActivity();
        JSMApplication.setDBItemsReference(JSMApplication.getFirebaseDatabase().getReference(Constants.ITEMS_PATH));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_details, container, false);
        TextView titleText = (TextView) rootView.findViewById(R.id.itemListTitleText);
        ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.itemListBackBtn);

        Bundle bundle = mActivity.getIntent().getExtras();
        final String categoryKey = bundle.getString(Constants.CATEGORY_KEY, null);
        final String stockType = bundle.getString(Constants.STOCK_TYPE, null);
        CategoryItemBean category = (CategoryItemBean) bundle.getSerializable(Constants.CATEGORY_BEAN);

        titleText.setText(stockType + "_" + category.getName());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        RecyclerView mRVCategoryList = (RecyclerView) rootView.findViewById(R.id.rvItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRVCategoryList.setLayoutManager(layoutManager);

        mItemList.clear();
        mItemList.addAll(category.getItemList().keySet());
        Collections.sort(mItemList);
        mRVAdapter = new RVItemListAdapter(mActivity, mItemList);
        mRVAdapter.setItemListMap(category.getItemList());
        mRVAdapter.setStockType(stockType);
        mRVCategoryList.setAdapter(mRVAdapter);

        // Read from the database
        JSMApplication.getDBItemsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    HashMap<String, HashMap<String, CategoryItemBean>> stockListMap = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, HashMap<String, CategoryItemBean>>>() {
                    });
                    JSMApplication.setStockTypeListMap(stockListMap);
                    HashMap<String, CategoryItemBean> categoryListMap = JSMApplication.getStockTypeListMap().get(stockType);
                    CategoryItemBean category = categoryListMap.get(categoryKey);
                    mItemList.clear();
                    mItemList.addAll(category.getItemList().keySet());
                    Collections.sort(mItemList);
                    mRVAdapter.setItemListMap(category.getItemList());
                    mRVAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "data is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());

            }
        });

        return rootView;
    }
}
