package com.espica.data;

import android.content.Context;
import com.espica.data.model.User;

public class EspicaManager {
    private static EspicaManager INSTANCE = null;
    private User user;

    private EspicaManager(Context context) {
        refreshUser(context);
    }

    public void refreshUser(Context context) {
        user = SettingManager.getInstance(context).loadUserInfo();
    }

    public synchronized static EspicaManager getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new EspicaManager(context);

        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void release() {
        user = null;
        INSTANCE = null;
    }

}
