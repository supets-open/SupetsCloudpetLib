package com.supets.commons.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * supets_shopmail
 *
 * @user lihongjiang
 * @description
 * @date 2017/4/5
 * @updatetime 2017/4/5
 */

public class FileProviderCompat {

    public static Intent getInstallIntent(Context context, Intent installIntent, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            //判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.supets.shop" + ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        return installIntent;
    }

    /**
     * 拍照，裁剪
     *
     * @param context
     * @param imgPath
     * @return
     */
    public static Uri getFileUri(Context context, String imgPath) {
        File imgFile = new File(imgPath);
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            //如果是7.0或以上，使用getUriForFile()获取文件的Uri
            imgUri = FileProvider.getUriForFile(context, "com.supets.shop" + ".fileprovider", imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }
        return imgUri;
    }
    public static Uri getFileUri(Context context, File imgFile) {
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            //如果是7.0或以上，使用getUriForFile()获取文件的Uri
            imgUri = FileProvider.getUriForFile(context, "com.supets.shop" + ".fileprovider", imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }
        return imgUri;
    }
}
