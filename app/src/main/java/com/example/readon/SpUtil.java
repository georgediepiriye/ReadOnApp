package com.example.readon;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SpUtil {
    public static final String PREF_NAME = "BooksPreferences";
    public static final String POSITION = "position";
    public static final String QUERY = "query";

    private SpUtil() {
    }

    //method for initialization of the shared preferences
    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //method to rad the string from the preferences
    public static String getPreferenceString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    //method to read the int from the preferences
    public static int getPreferenceInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    //method to write a string to the preferences
    public static void setPreferenceString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    //method to write an int to the preferences
    public static void setPreferenceInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //method to get the list of queries in the advanced search
    public static ArrayList<String> getQueryLIst(Context context) {
        ArrayList<String> queryList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String query = getPrefs(context).getString(QUERY + String.valueOf(i), "");
            assert query != null;
            if (!query.isEmpty()) {
                query = query.replace(",", " ");
                queryList.add(query.trim());
            }
        }
        return queryList;

    }
}
