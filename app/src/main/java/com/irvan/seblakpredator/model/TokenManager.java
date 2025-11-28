package com.irvan.seblakpredator.model;
import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "AUTH_PREF";
    private static final String KEY_TOKEN = "ACCESS_TOKEN";

    public static void saveToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);  // Menyimpan token ke SharedPreferences
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    public static void removeToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();
    }
}

