package com.vj.jewellerystockmanager.itemdetails;


import android.os.Bundle;
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
import java.util.HashMap;

/**
 * Fragment to show Items list
 */
public class CategoryDetailsFragment extends BaseFragment {

    private static final String TAG = "CategoryDetailsFragment";
    private CategoryDetailsActivity mActivity;
    private ArrayList<String> mCategoryList = new ArrayList<>();
    private RVCategoryListAdapter mRVAdapter;

    public CategoryDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mActivity = (CategoryDetailsActivity) getActivity();
        JSMApplication.setDBItemsReference(JSMApplication.getFirebaseDatabase().getReference(Constants.ITEMS_PATH));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category_details, container, false);
        TextView titleText = (TextView) rootView.findViewById(R.id.categoryTitleText);
        ImageButton backBtn = (ImageButton) rootView.findViewById(R.id.categoryBackBtn);

        Bundle bundle = mActivity.getIntent().getExtras();
        final String stockType = bundle.getString(Constants.STOCK_TYPE, null);

        titleText.setText(stockType);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        RecyclerView mRVCategoryList = (RecyclerView) rootView.findViewById(R.id.rvCategoryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRVCategoryList.setLayoutManager(layoutManager);

        HashMap<String, CategoryItemBean> categoryListMap = JSMApplication.getStockTypeListMap().get(stockType);

        mCategoryList.clear();
        mCategoryList.addAll(categoryListMap.keySet());

        mRVAdapter = new RVCategoryListAdapter(mActivity, mCategoryList);
        mRVAdapter.setCategoryListMap(categoryListMap);
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
                    mCategoryList.clear();
                    mCategoryList.addAll(categoryListMap.keySet());
                    mRVAdapter.setCategoryListMap(categoryListMap);
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
