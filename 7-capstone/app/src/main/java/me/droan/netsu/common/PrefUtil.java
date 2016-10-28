package me.droan.netsu.common;

import android.content.Context;
import android.content.SharedPreferences;

import me.droan.netsu.R;

/**
 * Created by Drone on 23/09/16.
 */

public class PrefUtil {

    public static boolean getPref(Context context, String key, boolean defaultFlag) {
        return getSharedPreferences(context).getBoolean(key, defaultFlag);
    }

    public static String getPref(Context context, String key, String defaultString) {
        return getSharedPreferences(context).getString(key, defaultString);
    }

    public static void setPref(Context context, String key, boolean flag) {
        getEditor(context).putBoolean(key, flag).commit();
    }

    public static void setPref(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }


    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPref = getSharedPreferences(context);
        return sharedPref.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_pref), Context.MODE_PRIVATE);
    }
}
