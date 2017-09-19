package com.supets.corenetwork.fresco;

import android.graphics.Bitmap;

public interface ImagePreloadListener {
        void onLoadingSuccess(Bitmap bitmap);
        void onLoadingFailed();
    }