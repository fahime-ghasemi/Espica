package com.espica.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.espica.data.model.User;

public class SettingManager {
    private static final String LOGIN_STATUS = "login_status";
    private static final String TOKEN = "token";

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;


    private static SettingManager INSTANCE = null;

    private SettingManager(Context context) {
        mSharedPreferences = context.getSharedPreferences("espica", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static synchronized SettingManager getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new SettingManager(context);
        return INSTANCE;
    }

    public void setLoginStatus(Boolean status) {
        mEditor.putBoolean(LOGIN_STATUS, status).apply();
    }

    public Boolean isLogin() {
        return mSharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public void setToken(String token) {
        mEditor.putString(TOKEN, token);
    }

    public String getToken() {
        return mSharedPreferences.getString(TOKEN, "");
    }

    User loadUserInfo() {
        User user = new User();
        user.setLogin(isLogin());
        return user;
    }
}
