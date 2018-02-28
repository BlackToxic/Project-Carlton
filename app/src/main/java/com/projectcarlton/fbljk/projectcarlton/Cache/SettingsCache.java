package com.projectcarlton.fbljk.projectcarlton.Cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.Data.User;

public class SettingsCache {

    private SettingsCache() {}

    private static SharedPreferences preferences;
    private static RequestQueue requestQueue;

    public static User CURRENTUSER;
    public static Group CURRENTGROUP;
    public static boolean SHOWPUSHNOTIFICATIONS;

    public static boolean isUserAdmin() {
        if (CURRENTUSER != null && CURRENTGROUP != null) {
            if (CURRENTGROUP.groupAdmin.equals(CURRENTUSER.userId)) {
                return true;
            }
        }

        return false;
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        return requestQueue;
    }

    public static void logout() {
        CURRENTGROUP = null;
        CURRENTUSER = null;
    }

    public static void setPreferences(SharedPreferences pref) {
        preferences = pref;

        loadSettings();
    }

    public static void saveSettings() {
        preferences.edit().putString("UserId", CURRENTUSER.userId).putString("UserName", CURRENTUSER.userName).putString("UserPassword", CURRENTUSER.userPassword).putBoolean("ShowPushNotifications", SHOWPUSHNOTIFICATIONS).apply();
    }

    public static void loadSettings() {
        SHOWPUSHNOTIFICATIONS = preferences.getBoolean("ShowPushNotifications", true);
    }
}
