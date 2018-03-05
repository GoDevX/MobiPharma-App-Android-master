/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

/**
 *
 */
package com.vamer.Pharma.pharmacyclientapp.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.vamer.Pharma.pharmacyclientapp.model.Customer;

/**
 * @author Hitesh
 */
public class PreferenceHelper {

    public final static String FIRST_TIME = "FirstTime";
    public final static String WHATS_NEW_LAST_SHOWN = "whats_new_last_shown";
    public final static String SUBMIT_LOGS = "CrashLogs";
    // Handle Local Caching of data for responsiveness
    public static final String MY_CART_LIST_LOCAL = "MyCartItems";
    private static final String USER_LOGGED_IN = "isLoggedIn";
    public static final String USER_CODE_VERIFCATION = "userCodeVerification";

    public static final String CUSTOMER_MOBILE = "MobNo";
    public static final String CUSTOMER_NAME = "Name";
    public static final String CUSTOMER_TOKEN = "CustomerToken";
    public static final String CUSTOMER_GENDER = "Gender";
    public static final String CUSTOMER_BIRTH_DATE = "BirthDate";


    //private static final String USER_TOKEN = "userCodeVerification";
    private static PreferenceHelper preferenceHelperInstance = new PreferenceHelper();

    private PreferenceHelper() {
    }

    public static PreferenceHelper getPrefernceHelperInstace() {

        return preferenceHelperInstance;
    }

    public void setBoolean(Context appContext, String key, Boolean value) {

        PreferenceManager.getDefaultSharedPreferences(appContext).edit()
                .putBoolean(key, value).apply();
    }

    public  void setCustomerData(Context c,String date,String phone,String token,String gender,String name) {
        setString(c,CUSTOMER_NAME,name);
        setString(c,CUSTOMER_MOBILE,phone);
        setString(c,CUSTOMER_GENDER,gender);
        setString(c,CUSTOMER_BIRTH_DATE,date);
        setString(c,CUSTOMER_TOKEN,token);

    }


    public  Customer getCustomerData(Context c) {
        Customer cu=new Customer( getString(c,CUSTOMER_BIRTH_DATE,""), getString(c,CUSTOMER_NAME,""), getString(c,CUSTOMER_TOKEN,""), getString(c,CUSTOMER_GENDER,""), getString(c,CUSTOMER_MOBILE,""));
        return cu;
    }


    public void setInteger(Context appContext, String key, int value) {

        PreferenceManager.getDefaultSharedPreferences(appContext).edit()
                .putInt(key, value).apply();
    }

    public void setFloat(Context appContext, String key, float value) {

        PreferenceManager.getDefaultSharedPreferences(appContext).edit()
                .putFloat(key, value).apply();
    }

    public void setString(Context appContext, String key, String value) {

        PreferenceManager.getDefaultSharedPreferences(appContext).edit()
                .putString(key, value).apply();
    }



    // To retrieve values from shared preferences:

    public boolean getBoolean(Context appContext, String key,
                              Boolean defaultValue) {

        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getBoolean(key, defaultValue);
    }

    public int getInteger(Context appContext, String key, int defaultValue) {

        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getInt(key, defaultValue);
    }

    public float getString(Context appContext, String key, float defaultValue) {

        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getFloat(key, defaultValue);
    }

    public String getString(Context appContext, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
                .getString(key, defaultValue);
    }

    public void setUserLoggedIn(boolean UserLoggedIn, Context appContext) {
        setBoolean(appContext, USER_LOGGED_IN, UserLoggedIn);
    }

    public boolean isUserLoggedIn(Context appContext) {
        return getBoolean(appContext, USER_LOGGED_IN, false);
    }


}
