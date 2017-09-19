package com.supets.commons.utils.file;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.supets.commons.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by b.s.lee on 2016/1/27.
 * <p/>
 * module   name:
 * module action:
 */
public class ImageUtils {

    public static final int Max_Width = 1024;
    public static final int Max_Height = 1024;
    public static final int Bitmap_Quality = 100;
    public static final int MINBitmap_Quality = 30;//不能低于30
    public static final int PhotoWidthMixLimit = 320;
    public static final int PhotoHeightMixLimit = 320;
    public static final int Photo_Max_Size = 300 * 1024; // 100-300k
    //固定所有图片显示最大范围
    public static final int Max_Scale_Width = 1024;
    public static final int Max_Scale_Height = 1024;

//    public static File addWaterMark(ViewGroup view) {
//        try {
//            if (view == null) {
//                return null;
//            }
//            Bitmap file = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config
//                    .ARGB_8888);
//            Canvas canvas = new Canvas(file);
//            view.draw(canvas);
//            Paint paint = new Paint();
//            Bitmap watermark = BitmapFactory.decodeResource(MyApplication.getInstance()
//                            .getResources(),
//                    R.drawable.watermark);
//            int fileWidth = file.getWidth();
//            int fileHeight = file.getHeight();
//            int watermarkWidth = watermark.getWidth();
//            int watermarkHeight = watermark.getHeight();
//            canvas.drawBitmap(watermark, fileWidth - watermarkWidth - 20, fileHeight -
//                    watermarkHeight - 20, paint);
//
//            File picture = FileUtils.createPhotoSavedFile();
//            FileOutputStream fos = new FileOutputStream(picture.getPath());
//            byte[] data = Bitmap2Bytes(file);
//            fos.write(data);
//            fos.close();
//            file.recycle();
//
//            return picture;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String insertImageToGallery(ContentResolver cr, Bitmap source, String title,
                                              String description, String mimetype) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        if (mimetype == null) {
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        } else {
            values.put(MediaStore.Images.Media.MIME_TYPE, mimetype);
        }
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                    if ("image/png".equalsIgnoreCase(mimetype)) {
                        format = Bitmap.CompressFormat.PNG;
                    }
                    source.compress(format, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                /*long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails
                .MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F,Images.Thumbnails.MICRO_KIND);*/
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config
                .ARGB_8888);
        // 利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        // 把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
    }

    public static boolean isValidateSize(Uri uri, int width, int height) {
        return isValidateSize(getImagePathFromUri(uri), width, height);
    }

    public static boolean isValidateSize(String imagePath, int width, int height) {
        int[] size = new int[2];
        getImageSize(imagePath, size);

        if (size[0] < width) {
            return false;
        }
        if (size[1] < height) {
            return false;
        }

        return true;
    }

    public static String getImagePathFromUri(Uri uri) {
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = App.INSTANCE.getContentResolver().query(uri,
                proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images
                .Media.DATA);
        actualimagecursor.moveToFirst();

        String imagePath = actualimagecursor.getString(actual_image_column_index);
        actualimagecursor.close();
        return imagePath;
    }

    public static void getImageSize(String imagePath, int[] size) {
        if (size == null || size.length < 2) {
            throw new IllegalArgumentException("size must be an array of two integers");
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;
    }

    public static void getImageSize(Uri uri, int[] size) {
        getImageSize(getImagePathFromUri(uri), size);
    }

    public static byte[] readPhotobyte(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int fileWidth = options.outWidth;
        int fileHeight = options.outHeight;

        options.inJustDecodeBounds = false;
        int inSampleSize = 1;
        if ((fileWidth > Max_Width) || fileHeight > Max_Height) {
            if (fileWidth > fileHeight) {
                inSampleSize = Math.round((float) fileHeight / (float) Max_Height);
            } else {
                inSampleSize = Math.round((float) fileWidth / (float) Max_Width);
            }
        }
        options.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap != null) {
            return Bitmap2Bytes(bitmap);
        }
        return null;
    }

    public static Bitmap readPhotoBitmap(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int fileWidth = options.outWidth;
        int fileHeight = options.outHeight;

        options.inJustDecodeBounds = false;
        int inSampleSize = 1;
        if ((fileWidth > Max_Width) || fileHeight > Max_Height) {
            if (fileWidth > fileHeight) {
                inSampleSize = Math.round((float) fileHeight / (float) Max_Height);
            } else {
                inSampleSize = Math.round((float) fileWidth / (float) Max_Width);
            }
        }
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap readPhotoBitmap(String filePath, int width, int height) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int originalFileHeight = options.outHeight;
        int originalfileWidth = options.outWidth;
        Log.v("FrescoImageHelper", originalfileWidth + "=before=" + originalFileHeight);

        options.inJustDecodeBounds = false;
        int inSampleSize = 1;
        if ((originalfileWidth > width)
                || originalFileHeight > height) {
            if (originalfileWidth > originalFileHeight) {
                inSampleSize = Math.round((float) originalFileHeight
                        / height);
            } else {
                inSampleSize = Math.round((float) originalfileWidth
                        / width);
            }
        }

        if (inSampleSize < 0) {
            inSampleSize = 1;
        }

        Log.v("FrescoImageHelper", inSampleSize + "=scale=");

        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        Log.v("FrescoImageHelper", bitmap.getWidth() + "=after=" + bitmap.getHeight());
        return bitmap;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, Bitmap_Quality, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //裁剪保存jpg图片,并且压缩
    public static byte[] Bitmap2Bytes(Bitmap bm, int MaxSize) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result = null;

        int compressQuality = Bitmap_Quality;
        while (true) {
            if (compressQuality <= 0) {
                break;
            }
            try {
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            output.reset();
            bm.compress(Bitmap.CompressFormat.JPEG, compressQuality, output);
            result = output.toByteArray();
            int total = output.size();

            if (compressQuality < MINBitmap_Quality) {//30
                Log.v("compressQuality", compressQuality + "==" + total * 1f / 1024);
                break;
            }

            if (total > MaxSize) {
                compressQuality = compressQuality - getOptionBi(total);
                Log.v("compressQuality2", compressQuality + "==" + total * 1f / 1024);
                continue;
            }

            Log.v("compressQuality3", compressQuality + "==" + total * 1f / 1024);
            break;

        }

        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //裁剪保存jpg图片,或者png图片,并且压缩
    public static void compressBmpToFile(File srcFile,File file){
        boolean ispng=srcFile.getAbsolutePath().toUpperCase().endsWith("PNG");
        Bitmap bmp=BitmapFactory.decodeFile(srcFile.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 95;//80
        int min=MINBitmap_Quality;
        if (ispng){
            min=55;
        }
        bmp.compress(!ispng?Bitmap.CompressFormat.JPEG: Bitmap.CompressFormat.PNG, options, baos);

        long  total=baos.toByteArray().length;
        Log.v("getOptionBi1",options+"==="+total/1024);
        while (total > Photo_Max_Size) {
            options -= getOptionBi(total);
            Log.v("getOptionBi2",options+"==="+total/1024);
            if (options>=min){
                baos.reset();
                bmp.compress(!ispng?Bitmap.CompressFormat.JPEG: Bitmap.CompressFormat.PNG, options, baos);
                total=baos.toByteArray().length;
                Log.v("getOptionBi2",options+"==="+total/1024);
            }else{
                break;
            }

        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getOptionBi(long total) {
        int scale=(int) (total *1f /Photo_Max_Size *5);
        return scale<=0?10:scale;
    }

    public static int getImageWidth(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outWidth;
    }

    public static int getImageHeight(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outHeight;
    }

    //处理png
    public static File compressScale(File mSourceUri, int width, int height, File outFile) {

        if (width == 0) {
            width = ImageUtils.Max_Width;
        }
        if (height == 0) {
            height = ImageUtils.Max_Height;
        }

        InputStream in = null;
        try {
            in = App.INSTANCE.getContentResolver().openInputStream(Uri.fromFile(mSourceUri));

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);

            if (in != null)
                in.close();

            int scale = 1;
            if (o.outHeight > height
                    || o.outWidth > width) {
                if (o.outWidth < o.outHeight) {
                    scale = Math.round((float) o.outHeight
                            / (float) height);
                } else {
                    scale = Math.round((float) o.outWidth
                            / (float) width);
                }
            }

            if (scale == 1) {
                return mSourceUri;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = App.INSTANCE.getContentResolver().openInputStream(Uri.fromFile(mSourceUri));
            Bitmap mBitmap = BitmapFactory.decodeStream(in, null, o2);
            FileOutputStream fos = new FileOutputStream(outFile);
            if(!mSourceUri.getAbsolutePath().toUpperCase().endsWith("PNG")){
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }else{
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            fos.close();
            return outFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void scanPicture(File path, Context ct) {
        ct.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                .fromFile(path)));
    }

    public static void saveToGallery(File path, Context ct) {
        try {
            MediaStore.Images.Media.insertImage(ct.getContentResolver(), path.getAbsolutePath(),
                    path.getName(), null);
        } catch (FileNotFoundException e) {
        }
    }

}
