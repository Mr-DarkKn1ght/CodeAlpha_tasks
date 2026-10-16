package com.example.healthtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    private static final String PREF = "health_tracker";
    private final SharedPreferences p;

    public AppPrefs(Context context) {
        p = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void put(String key, String value) { p.edit().putString(key, value).apply(); }
    public String get(String key, String def) { return p.getString(key, def); }
    public void putInt(String key, int value) { p.edit().putInt(key, value).apply(); }
    public int getInt(String key, int def) { return p.getInt(key, def); }
    public void putFloat(String key, float value) { p.edit().putFloat(key, value).apply(); }
    public float getFloat(String key, float def) { return p.getFloat(key, def); }
    public void clearLogin() { p.edit().putBoolean("loggedIn", false).apply(); }
    public boolean isLoggedIn() { return p.getBoolean("loggedIn", false); }
}
