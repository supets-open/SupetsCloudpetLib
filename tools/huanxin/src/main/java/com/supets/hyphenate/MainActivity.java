package com.supets.hyphenate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.NetUtils;


public class MainActivity extends AppCompatActivity {

    MyConnectionListener myConnectionListener = new MyConnectionListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMClient.getInstance().addConnectionListener(myConnectionListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginout();
        EMClient.getInstance().removeConnectionListener(myConnectionListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
    }

    private void login() {
        EMClient.getInstance().login("lihongjiang", "lihongjiang", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");


                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, "lihongjiang2"));

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });

    }

    private void loginout() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d("main", "退出聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "退出聊天服务器失败！");
            }
        });
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Log.d("EMConnectionListener", "显示帐号已经被移除");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Log.d("EMConnectionListener", "显示帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            Log.d("EMConnectionListener", "连接不到聊天服务器！");
                        } else {
                            //当前网络不可用，请检查网络设置
                            Log.d("EMConnectionListener", "当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }
}
