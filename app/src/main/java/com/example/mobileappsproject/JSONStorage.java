package com.example.mobileappsproject;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class JSONStorage {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "prefs";

    public JSONStorage() {
        // Blank
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString("username_key", "default_username");
    }

    public static void setUsername(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("username_key", input);
        editor.commit();
    }
//    private static void saveJSON(String myList, JSONObject jObj) {
//        SharedPreferences.Editor prefEditor = getSharedPreferences( myList, Context.MODE_PRIVATE).edit();
//        prefEditor.putString( myList, jObj.toString() );
//        prefEditor.commit();
//        System.out.println("Saving JSON");
//    }

//    private String readJSON(String myList) {
//        SharedPreferences prefs = this.getSharedPreferences(myList, Context.MODE_PRIVATE);
//        String savedJSON = prefs.getString(myList, "0");
//        return savedJSON;
//    }
}