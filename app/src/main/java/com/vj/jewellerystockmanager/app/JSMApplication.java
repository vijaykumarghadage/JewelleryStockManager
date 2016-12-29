package com.vj.jewellerystockmanager.app;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vj.jewellerystockmanager.itemdetails.CategoryItemBean;

import java.util.HashMap;

/**
 * This an application class
 * Created by vijaykumargh on 08/08/2016.
 */
public class JSMApplication extends Application {

    private static Context sContext;
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDBItemsRef;
    private static HashMap<String, HashMap<String, CategoryItemBean>> sStockTypeListMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    /**
     * Use this method to get wherever application level context is required.
     *
     * @return application context
     */
    public static Context getAppContext() {
        return sContext;
    }


    public static HashMap<String, HashMap<String, CategoryItemBean>> getStockTypeListMap() {
        return sStockTypeListMap;
    }

    public static void setStockTypeListMap(HashMap<String, HashMap<String, CategoryItemBean>> stockTypeListMap) {
        sStockTypeListMap = stockTypeListMap;
    }

    public static DatabaseReference getDBItemsReference() {
        return mDBItemsRef;
    }

    public static void setDBItemsReference(DatabaseReference dbItemsRef) {
        mDBItemsRef = dbItemsRef;
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public static void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        mFirebaseDatabase = firebaseDatabase;
    }

}
