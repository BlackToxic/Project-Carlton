package com.projectcarlton.fbljk.projectcarlton.Cache;

import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.Data.User;

public class SettingsCache {

    private SettingsCache() {}

    public static User CURRENTUSER;
    public static Group CURRENTGROUP;

    public static boolean isUserAdmin() {
        if (SettingsCache.CURRENTUSER != null && SettingsCache.CURRENTGROUP != null) {
            if (SettingsCache.CURRENTGROUP.groupAdmin.equals(SettingsCache.CURRENTUSER.userId)) {
                return true;
            }
        }

        return false;
    }

}
