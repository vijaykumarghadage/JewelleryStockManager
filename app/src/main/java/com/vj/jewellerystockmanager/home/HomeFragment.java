package com.vj.jewellerystockmanager.home;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.vj.jewellerystockmanager.R;
import com.vj.jewellerystockmanager.app.JSMApplication;
import com.vj.jewellerystockmanager.framework.global.Constants;
import com.vj.jewellerystockmanager.framework.ui.BaseFragment;
import com.vj.jewellerystockmanager.itemdetails.CategoryItemBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Home fragment
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";
    private RVStockTypeListAdapter mRVAdapter;
    private ProgressDialog mProgressDialog;
    private MainActivity mActivity;
    private ArrayList<String> mStockTypeList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mActivity = (MainActivity) getActivity();

        JSMApplication.setFirebaseDatabase(FirebaseDatabase.getInstance());
        JSMApplication.setDBItemsReference(JSMApplication.getFirebaseDatabase().getReference(Constants.ITEMS_PATH));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        showProgressDialog(getActivity(), getActivity().getString(R.string.loading));

        RecyclerView mRVStockTypeList = (RecyclerView) rootView.findViewById(R.id.rvStockTypeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRVStockTypeList.setLayoutManager(layoutManager);

        mStockTypeList.clear();
        mStockTypeList.addAll(JSMApplication.getStockTypeListMap().keySet());

        mRVAdapter = new RVStockTypeListAdapter(mActivity, mStockTypeList);
        mRVStockTypeList.setAdapter(mRVAdapter);

        // Read from the database
        JSMApplication.getDBItemsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    HashMap<String, HashMap<String, CategoryItemBean>> stockListMap = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, HashMap<String, CategoryItemBean>>>() {
                    });
                    Log.d(TAG, "StockTypeList: " + stockListMap.toString());

                    mStockTypeList.clear();
                    mStockTypeList.addAll(stockListMap.keySet());
                    JSMApplication.setStockTypeListMap(stockListMap);
                    mRVAdapter.notifyDataSetChanged();
                    stopProgressDialog();
                } else {
                    Log.d(TAG, "data is null");
                    stopProgressDialog();
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

    /**
     * Method used to show progress bar
     *
     * @param message message to be shown
     */
    public void showProgressDialog(Context context, String message) {
        mProgressDialog = ProgressDialog.show(context, "", message, true);
        mProgressDialog.setCancelable(false);
    }

    /**
     * Method used to stop the progress bar
     */
    public void stopProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

}
