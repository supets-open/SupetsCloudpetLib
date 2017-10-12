package com.supets.tools.walle.channel;

import android.content.Context;

import com.meituan.android.walle.WalleChannelReader;
import com.supets.commons.MiaCommons;

import java.io.File;

public class ChannelUtil {


    public static String getChannel(Context context) {
        if (isAppNewInstalled()) { // 新启动且新安装的
            String channel = WalleChannelReader.getChannel(context.getApplicationContext());
            ChannelPref.saveChannelCode(channel);
            return channel;
        }
        return ChannelPref.getChannelCode();
    }

    private static boolean isAppNewInstalled() {
        long appLastUpdateTime = ChannelPref.getAppLastUpdateTime();
        long newestUpdateTime = getAppLastUpdateTimeFromApk();
        if (appLastUpdateTime != newestUpdateTime) {
            ChannelPref.saveAppLastUpdateTime(newestUpdateTime);
            return true;
        }
        return false;
    }

    private static long getAppLastUpdateTimeFromApk() {
        String appSourceDir = MiaCommons.getContext().getApplicationInfo().sourceDir;
        return new File(appSourceDir).lastModified();
    }

}
