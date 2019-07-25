package com.espica.data;

import android.content.Context;
import com.espica.data.model.User;

public class EspicaManager {
    private static EspicaManager INSTANCE = null;
    private User user;

    private EspicaManager() {
        refreshUser();
    }

    public void refreshUser() {
        user = SettingManager.getInstance().loadUserInfo();
    }

    public synchronized static EspicaManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new EspicaManager();

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
