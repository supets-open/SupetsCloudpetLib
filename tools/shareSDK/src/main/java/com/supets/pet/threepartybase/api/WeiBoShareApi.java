package com.supets.pet.threepartybase.api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.supets.pet.threepartybase.model.AuthToken;
import com.supets.pet.threepartybase.model.WeiBoToken;
import com.supets.pet.threepartybase.storage.WeiboPref;
import com.supets.pet.threepartybase.utils.OauthListener;

public class WeiBoShareApi {


    public static RequestListener mlistener;

    public static void shareToWeibo(Activity activity, String content, Bitmap bitmap, RequestListener listener) {
//        Oauth2AccessToken accesstoken = WeiboPref.readOauth2AccessToken();
//        StatusesAPI statusApi = new StatusesAPI(activity, KeyAndSecrets.SINAWEIBO_APPKEY, accesstoken);
//        statusApi.upload(content, bitmap, null, null, listener);

        mlistener = listener;

        initShare(activity);

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (content != null) {
            TextObject textObject = new TextObject();
            textObject.text = content;
            textObject.title = "万宠";
            textObject.actionUrl = "http://m.10000pets.com";
            weiboMessage.textObject = textObject;
        }

        if (bitmap != null) {
            ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(bitmap);
            weiboMessage.imageObject = imageObject;
        }

        if (shareHandler != null) {
            shareHandler.shareMessage(weiboMessage, false);
        }

    }

    public static void checkOauthValid(Activity activity, final OauthListener mOauth) {
        if (WeiboPref.isSinaWeiboAuthorized()) {
            mOauth.OauthSuccess(WeiboPref.readAccessToken());
        } else {
            WeiBoAuthApi.login(activity, new OauthListener() {
                @Override
                public void OauthSuccess(AuthToken token) {
                    WeiboPref.saveAccessToken((WeiBoToken) token);
                    mOauth.OauthSuccess(WeiboPref.readAccessToken());
                }

                @Override
                public void OauthFail() {
                    mOauth.OauthFail();
                }

                @Override
                public void OauthCancel() {
                    mOauth.OauthFail();
                }
            }, null);
        }

    }


    private static WbShareHandler shareHandler;

    public static void initShare(Activity activity) {
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
    }

    public static void onNewIntent(Intent intent) {
        if (shareHandler != null) {
            shareHandler.doResultIntent(intent, new WbShareCallback() {
                @Override
                public void onWbShareSuccess() {
                    if (mlistener != null) {
                        mlistener.onComplete("");
                    }
                }

                @Override
                public void onWbShareCancel() {
                    if (mlistener != null) {
                        mlistener.onWeiboException(null);
                    }
                }

                @Override
                public void onWbShareFail() {
                    if (mlistener != null) {
                        mlistener.onWeiboException(null);
                    }
                }
            });
        }
    }

    public  static void onDestory(){
        shareHandler=null;
        mlistener=null;
    }

}
