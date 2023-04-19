package com.example.licenta;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_API_KEY_1 = "API_KEY_1";
    private static final String KEY_CHANNEL_ID_1 = "CHANNEL_ID_1";
    private static final String KEY_API_KEY_2 = "API_KEY_2";
    private static final String KEY_CHANNEL_ID_2 = "CHANNEL_ID_2";
    private static final String KEY_API_KEY_3 = "API_KEY_3";
    private static final String KEY_CHANNEL_ID_3 = "CHANNEL_ID_3";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //API getters
    public String getApiKey1() {
        return sharedPreferences.getString(KEY_API_KEY_1, "486R5S7M0349NF3V");
    }

    public String getApiKey2() {
        return sharedPreferences.getString(KEY_API_KEY_2, "NVON0G6E6POWKGRP");
    }

    public String getApiKey3() {
        return sharedPreferences.getString(KEY_API_KEY_3, "1ZCLKDX2REMRH31O");
    }

    //API setters
    public void setApiKey1(String apiKey1) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_API_KEY_1, apiKey1);
        editor.apply();
    }

    public void setApiKey2(String apiKey2) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_API_KEY_2, apiKey2);
        editor.apply();
    }

    public void setApiKey3(String apiKey3) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_API_KEY_3, apiKey3);
        editor.apply();
    }

    //channel_id getters
    public String getChannelId1() {
        return sharedPreferences.getString(KEY_CHANNEL_ID_1, "2039388");
    }

    public String getChannelId2() {
        return sharedPreferences.getString(KEY_CHANNEL_ID_2, "2071204");
    }

    public String getChannelId3() {
        return sharedPreferences.getString(KEY_CHANNEL_ID_3, "2071205");
    }

    //channel_id setters
    public void setChannelId1(String channelId1) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CHANNEL_ID_1, channelId1);
        editor.apply();
    }

    public void setChannelId2(String channelId2) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CHANNEL_ID_2, channelId2);
        editor.apply();
    }

    public void setChannelId3(String channelId3) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CHANNEL_ID_3, channelId3);
        editor.apply();
    }
}
