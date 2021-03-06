package com.supets.commons.utils;

import android.content.Context;
import android.os.Build;


public class ClipMananger {

    /**
     * 实现文本复制功能
     */
    public static void copy(String content, Context context) {
        if (Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager cmb = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        } else {
            android.text.ClipboardManager cmb = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        }
    }

    /**
     * 实现粘贴功能
     */
    public static String paste(Context context) {
        if (Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager cmb = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            return cmb.getText().toString().trim();
        } else {
            android.text.ClipboardManager cmb = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            return cmb.getText().toString().trim();
        }
    }


}
