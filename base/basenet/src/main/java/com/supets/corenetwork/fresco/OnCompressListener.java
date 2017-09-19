package com.supets.corenetwork.fresco;

import java.io.File;

public interface OnCompressListener {
        void onSuccess(File file);
        void onError(Throwable e);
    }