package com.supets.corenetwork.fresco;

import android.net.Uri;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class ResizeManager {

    static ImageRequestBuilder getResizeImageRequest(Uri formUri) {

        if (android.os.Build.VERSION.SDK_INT <14){
            return ImageRequestBuilder.newBuilderWithSource(formUri)
                    .setResizeOptions(new ResizeOptions(480,480));
        }
        //不限制Resize
        return ImageRequestBuilder.newBuilderWithSource(formUri);
    }

}
