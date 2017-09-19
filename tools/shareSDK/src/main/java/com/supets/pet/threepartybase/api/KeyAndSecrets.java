package com.supets.pet.threepartybase.api;

/**
 * ThreePartyPlatform
 *
 * @user lihongjiang
 * @description
 * @date 2016/9/7
 * @updatetime 2016/9/7
 */
public interface KeyAndSecrets {

    // 微信APP ID
    String WEIXINAPPID = "wx6f97138c4325187f";
    String WEIXIN_SCOPE = "snsapi_userinfo";
    String WEIXIN_APPSECRET = "00cbe5309884ed55af6063aaeae8dcbe";
    String WEIXIN_MERCHANT_ID = "1459721102";

    // sina weibo APP KEY
    String SINAWEIBO_APPKEY = "2465859435";
    String SINAWEIBO_REDIRECT_URI = "https://api.weibo.com/oauth2/default.html";
    String SINAWEIBO_AppSecret = "f0d22cce45164ff9c7840cb7827bc0ea";
    String SINAWEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    // qq APP ID
    String QQ_APPID = "1106010011";
    String QQ_SCOPE = "all";
}
