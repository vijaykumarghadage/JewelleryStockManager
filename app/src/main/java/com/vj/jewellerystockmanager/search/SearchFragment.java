package com.vj.jewellerystockmanager.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.framework.ui.BaseFragment;
import com.vj.jewellerystockmanager.itemdetails.ItemBean;
import com.vj.jewellerystockmanager.itemdetails.RVItemListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Fragment for searching items
 * Created by vijaykumargh on 11/08/2016.
 */
public class SearchFragment extends BaseFragment {

    private static final String TAG = "SearchFragment";
    private RecyclerView mItemListSearchRV;
    private SearchActivity mActivity;
    private ArrayList<ItemBean> mItemList = new ArrayList<>();
    private ArrayList<ItemBean> mFilteredList = new ArrayList<>();
    private RVItemListAdapter mRVAdapter;
    private CharSequence mQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (SearchActivity) getActivity();
        JSMApplication.setDBItemsReference(JSMApplication.getFirebaseDatabase().getReference(Constants.ITEMS_PATH));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        EditText searchItemEditText = (EditText) view.findViewById(R.id.searchItemEditText);
        ImageView backBtn = (ImageView) view.findViewById(R.id.searchBackBtn);

        mItemListSearchRV = (RecyclerView) view.findViewById(R.id.rvItemListSearch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mItemListSearchRV.setLayoutManager(layoutManager);

        //mItemList.addAll(JSMApplication.getStockTypeListMap().values());
        mFilteredList.addAll(mItemList);
        Log.i(TAG, "List Size: " + mItemList.size());
        //mRVAdapter = new RVItemListAdapter(mActivity, mFilteredList);
        mItemListSearchRV.setAdapter(mRVAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        
        searchItemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                mQuery = query.toString().toLowerCase();
                updateList();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // Read from the database
        JSMApplication.getDBItemsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    mItemList.clear();
                    HashMap<String, ItemBean> iHashMap = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ItemBean>>() {
                    });
                    mItemList.addAll(iHashMap.values());
                    Collections.sort(mItemList, mActivity.new ItemIdComparator());
                    Log.d(TAG, "ItemList: " + mItemList.toString());
                   // JSMApplication.setStockTypeListMap(iHashMap);
                    updateList();
                    //mRVAdapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, "ItemList is null");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });

        return view;
    }

    private void updateList() {
        mFilteredList.clear();
        if (!mQuery.toString().isEmpty()) {
            for (int i = 0; i < mItemList.size(); i++) {
                ItemBean item = mItemList.get(i);
                final String name = item.getItemName().toLowerCase();
                final String id = String.valueOf(item.getItemId()).toLowerCase();
                final String qty = String.valueOf(item.getQuantity()).toLowerCase();
                final String wt = String.valueOf(item.getWeight()).toLowerCase();
                final String purity = String.valueOf(item.getPurity()).toLowerCase();
                if (name.contains(mQuery) || id.contains(mQuery) || qty.contains(mQuery)
                        || wt.contains(mQuery) || purity.contains(mQuery)) {
                    mFilteredList.add(mItemList.get(i));
                }
            }
        } else {
            mFilteredList.addAll(mItemList);
        }
        mRVAdapter.notifyDataSetChanged();  // data set changed
    }
}
