package com.supets.tools.walle.channel;

public class ChannelPref extends BasePref {

    private static final String DEFAULT_CHANNEL = "localhost";

    private static final String ChannelCode = "supets_channel_code";
    private static final String ChannelUpdateTime = "supets_channel_update_time";
    private static final String AppLastUpdateTime = "app_last_update_time";

    private static final String Name = "channel_config";

    /**
     * 保存渠道信息和渠道更新时间
     **/
    public static void saveChannelCode(String channel) {
        edit(Name).putString(ChannelCode, channel).putLong(ChannelUpdateTime, System.currentTimeMillis()).commit();
    }

    public static void clearChannelCode() {
        edit(Name).remove(ChannelCode).remove(ChannelUpdateTime).commit();
    }

    public static String getChannelCode() {
        return getPref(Name).getString(ChannelCode, DEFAULT_CHANNEL);
    }

    public static long getChannelUpdateTime() {
        return getPref(Name).getLong(ChannelUpdateTime, 0);
    }

    /**
     * 保存APP更新时间
     **/
    public static long getAppLastUpdateTime() {
        return getPref(Name).getLong(AppLastUpdateTime, 0);
    }

    public static void saveAppLastUpdateTime(long updateTime) {
        edit(Name).putLong(AppLastUpdateTime, updateTime).commit();
    }

    public static void clear() {
        BasePref.clear(Name);
    }

}
