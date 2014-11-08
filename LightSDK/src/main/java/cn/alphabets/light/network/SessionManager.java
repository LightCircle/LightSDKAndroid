package cn.alphabets.light.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 罗浩 on 14/11/7.
 */
public class SessionManager {

    public static String Scheme;
    public static String Host;
    public static String Port;

    private static final String StorePrefix = "cn.alphabets.light.network";
    private static final String CookieStoreKey = "cn.alphabets.light.network.cookie";
    private static final String CSRFStoreKey = "cn.alphabets.light.network.csrf";
    private static String CookieString;
    private static String CSRFString;

    public static String getCookieString(Context ctx) {
        if (CookieString != null) {
            return CookieString;
        } else {
            SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE);
            CookieString = sp.getString(CookieStoreKey, null);
            return CookieString;
        }
    }

    public static void setCookieString(Context ctx, String newStr) {
        if (newStr == null) {
            return;
        }
        if (CookieString == null || !CookieString.equals(newStr)) {
            CookieString = newStr;
            SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString(CookieStoreKey, newStr);
            e.commit();
        }
    }

    public static String getCSRFString(Context ctx) {
        if (CSRFString != null) {
            return CSRFString;
        } else {
            SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE);
            CSRFString = sp.getString(CSRFStoreKey, null);
            return CSRFString;
        }
    }

    public static void setCSRFString(Context ctx, String newStr) {
        if (newStr == null) {
            return;
        }
        if (CSRFString == null || !CSRFString.equals(newStr)) {
            CSRFString = newStr;
            SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString(CSRFStoreKey, newStr);
            e.commit();
        }
    }
}
