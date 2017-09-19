package com.supets.commons.utils;

import android.content.pm.ApplicationInfo;

import com.supets.commons.MiaCommons;

public class DebugUtils {

    public static boolean isDebug() {
        ApplicationInfo applicationInfo = MiaCommons.getContext().getApplicationInfo();
        return applicationInfo != null
                && (applicationInfo.flags &
                ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

}
