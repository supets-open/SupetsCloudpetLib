package com.supets.commons.utils.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.supets.commons.App;
import com.supets.commons.utils.MiaTextUtils;
import com.supets.commons.utils.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

    public static File getAppFolder() {
        if (!isSDCardAvailable()) {
            return null;
        }
       String folderName = MiaTextUtils.getString(R.string.app_sd_root_dir);
        File folder = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile() && file.exists()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 检测SD卡是否可用
     *
     * @return True if SD card is can be written, false otherwise.
     */
    public static boolean isSDCardAvailable() {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            App.toast("SD卡不可用");
            return false;
        }
        return true;
    }

    public static File createPhotoSavedFile(File file) {
        File folder = getAppFolder();
        if (folder == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.CHINA);
        if(!file.getAbsolutePath().toUpperCase().endsWith("PNG")){
            String fname = sdf.format(new Date()) + ".jpg";
            return new File(folder, fname);
        }else{
            String fname = sdf.format(new Date()) + ".png";
            return new File(folder, fname);
        }
    }

    public static File createPhotoSavedFile() {
        File folder = getAppFolder();
        if (folder == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.CHINA);
        String fname = sdf.format(new Date()) + ".jpg";
        return new File(folder, fname);
    }

    public static String readStringFromAsset(String filename) {
        try {
            InputStream inputStream = com.supets.commons.utils.UIUtils.getContext().getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringWriter writer = new StringWriter();

            String line = null;

            while ((line = reader.readLine()) != null) {
                writer.append(line);
            }

            reader.close();
            writer.close();

            return writer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String  compressFile(String mPicturePath,int photo_max_size) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mPicturePath);
            File file = FileUtils.createPhotoSavedFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(ImageUtils.Bitmap2Bytes(bitmap, photo_max_size));
            fos.close();
            bitmap.recycle();
            return  file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
