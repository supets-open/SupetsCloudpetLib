package com.supets.pet.threepartybase.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.supets.pet.threepartybase.model.WeiBoToken;
import com.supets.pet.threepartybase.utils.OauthListener;
import com.supets.pet.threepartybase.utils.OauthLoginListener;

/**
 * ThreePartyPlatform
 *
 * @user lihongjiang
 * @description
 * @date 2016/9/10
 * @updatetime 2016/9/10
 */
public class WeiBoAuthApi {

    private static SsoHandler mSinaWeiboSsoHandler;

    public static void login(final Activity activity,
                             final OauthListener listener,
                             final OauthLoginListener oauth) {
        getSsoHandler(activity).authorize(new WbAuthListener() {

            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                final Oauth2AccessToken accessToken =oauth2AccessToken;

                if (accessToken.isSessionValid()) {
                    final WeiBoToken token = new WeiBoToken();
                    token.access_token = accessToken.getToken();
                    token.uid = accessToken.getUid();
                    token.refresh_token = accessToken.getRefreshToken();
                    token.expires_time = accessToken.getExpiresTime();
                    // 正在为你登录，请稍候
                    listener.OauthSuccess(token);

                    if (oauth!=null){
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                WeiBoLoginApi.getUserInfo(activity,accessToken,token, oauth);
                            }
                        }).start();
                    }
                    Log.e("Login", "success");
                } else {
                    listener.OauthFail();
                }
            }

            @Override
            public void cancel() {
                listener.OauthCancel();
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                listener.OauthFail();
            }
        });
    }

    public static SsoHandler getSsoHandler(Activity activity) {
        mSinaWeiboSsoHandler = new SsoHandler(activity);
        return mSinaWeiboSsoHandler;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSinaWeiboSsoHandler != null) {
            mSinaWeiboSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            mSinaWeiboSsoHandler = null;
        }
    }

    //  application初始化
    public  static void weiboinit(Context context){
        WbSdk.install(context,new AuthInfo(context, KeyAndSecrets.SINAWEIBO_APPKEY,
                KeyAndSecrets.SINAWEIBO_REDIRECT_URI, KeyAndSecrets.SINAWEIBO_SCOPE));
    }

}
