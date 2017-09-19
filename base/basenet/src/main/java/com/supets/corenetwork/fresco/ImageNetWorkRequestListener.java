package com.supets.corenetwork.fresco;

import android.util.Log;

import com.facebook.imagepipeline.listener.BaseRequestListener;
import com.facebook.imagepipeline.request.ImageRequest;

/**
 * 图片请求监听器
 */
public class ImageNetWorkRequestListener extends BaseRequestListener {

    public static final String TAG = "BaseRequestListener";

    @Override
    public void onRequestStart(ImageRequest request, Object callerContext, String requestId,
                               boolean isPrefetch) {
        super.onRequestStart(request, callerContext, requestId, isPrefetch);
        Log.d(TAG,"图片ID:" + requestId+"开始请求时间:" + System.currentTimeMillis());
    }

    public void onRequestSuccess(ImageRequest request, String requestId, boolean isPrefetch) {
        Log.v(TAG, "onRequestSuccess:" + request.getSourceFile().getAbsolutePath());
        Log.v(TAG,"图片ID:" + requestId + "结束请求时间:" + System.currentTimeMillis());
        Log.v(TAG,"图片ID:" + requestId + "是否取自本地:" + isPrefetch);
    }

    public void onRequestFailure(ImageRequest request, String requestId, Throwable throwable,
                                 boolean isPrefetch) {
        Log.v(TAG, "onRequestFailure:" + request.getSourceFile().getAbsolutePath());
    }
}