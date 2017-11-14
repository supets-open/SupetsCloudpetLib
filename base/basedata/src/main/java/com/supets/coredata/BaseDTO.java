package com.supets.coredata;

import android.support.annotation.Keep;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import java.io.Serializable;

/**
 * 与服务端通信的协议基类DTO对象
 * <li> 实现了序列化协议，gson解析支持</li>
 */
@Keep
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    public int code;
    /**
     * 错误码英文提示
     */
    public String msg;
    /**
     * 错误码对话框提示
     */
    public String alert;

    public boolean isSucceeded() {
        return (code == 200);
    }

    /**
     * 主要用于数据缓存和同步，该方法建议使用在非UI线程
     */
    @WorkerThread
    @UiThread
    public void updateData() {
    }
}
