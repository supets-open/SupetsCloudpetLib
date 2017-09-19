package com.supets.commons.utils;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.supets.commons.MiaCommons;

import java.io.File;
import java.util.List;

public class MYUtils {

    public static void showToastMessage(int resId) {
        showToastMessage(MiaCommons.getContext().getString(resId));
    }

    public static void setTextColor(@ColorRes int colorRes, TextView... texts) {
        for (int i = 0; i < texts.length; i++) {
            texts[i].setTextColor(UIUtils.getColor(colorRes));
        }
    }

    public static void showToastMessage(String toastString) {
        if (TextUtils.isEmpty(toastString)) {
            return;
        }

        Toast toast = Toast.makeText(MiaCommons.getContext(), toastString, Toast.LENGTH_SHORT);
        toast.show();
    }


    public  static void showToastMessageCENTER(String toastString) {
        if (TextUtils.isEmpty(toastString)) {
            return;
        }
        Toast toast = Toast.makeText(MiaCommons.getContext(), toastString,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public  static void showToastMessageTOP(int toastString) {
        Toast toast = Toast.makeText(MiaCommons.getContext(), toastString,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public static String getVersionForServer() {
        String version = SystemUtils.getAppVersionName();
        if (!TextUtils.isEmpty(version)) {
            version = version.replace(".", "_");
            version = "android_" + version;
            return version;
        }
        return null;
    }

    public static String getVersionNum() {
        String version = SystemUtils.getAppVersionName();
        if (!TextUtils.isEmpty(version)) {
            version = "V" + version;
            return version;
        }
        return null;
    }

    public static boolean getIsInstallApk(Context context, String apkPath) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        String sign = SignatureUtils.getSign(context);
        if (TextUtils.isEmpty(sign)) {
            MYUtils.showToastMessage(R.string.get_install_sign_failure);
            return true;
        }
        String uninstallAPKSignatures;
        uninstallAPKSignatures = currentApiVersion >= 14 ? SignatureUtils.getUnInstallApkSign(context, apkPath) : SignatureUtils.getUninstallAPKSignatures(apkPath);
        if (TextUtils.isEmpty(uninstallAPKSignatures)) {
            MYUtils.showToastMessage(R.string.get_uninstall_sign_failure);
            return true;
        }
        if (!TextUtils.isEmpty(sign) && !TextUtils.isEmpty(uninstallAPKSignatures)) {
            if (sign.equals(uninstallAPKSignatures)) {
                return true;
            } else {
                MYUtils.showToastMessage(R.string.sign_different);
                return false;
            }
        }
        return false;
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取cache的size
     */
    public static String getCacheSize(File file) throws Exception {
        return Formatter.formatFileSize(MiaCommons.getContext(), getFolderSize(file));
    }

    public static void setViewVisible(View  view,boolean isShow){
        view.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    public static boolean  isNoMoreData(int total,int page,int pageSize){
        return total<=page*pageSize;
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
