package com.supets.corenetwork.fresco;

import android.util.Log;

import com.supets.commons.utils.file.FileUtils;
import com.supets.commons.utils.file.ImageUtils;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class lubanUtils {

    public static void compressFile(final File file, final OnCompressListener listener) {

        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {
                    long starttime = System.currentTimeMillis();
                    Log.v("start-time0", System.currentTimeMillis() - starttime + "");
                    File out = FileUtils.createPhotoSavedFile(file);
                    File scaleFile = ImageUtils.compressScale(file, ImageUtils.Max_Width, ImageUtils.Max_Height, out);
                    Log.v("start-time1", System.currentTimeMillis() - starttime + "");
                    if (scaleFile == null) {
                        subscriber.onError(new Throwable("compress fail"));
                        return;
                    }

                    File out2 = FileUtils.createPhotoSavedFile();
                    ImageUtils.compressBmpToFile(scaleFile, out2);
                    if (out2.exists()) {
                        Log.v("start-time2", System.currentTimeMillis() - starttime + "");
                        subscriber.onNext(out2);
                    } else {
                        subscriber.onError(new Throwable("compress fail"));
                    }
                } catch (Exception e) {
                    subscriber.onError(new Throwable("compress fail"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) listener.onError(e);
                    }

                    @Override
                    public void onNext(File file) {
                        if (listener != null) listener.onSuccess(file);
                    }
                });
    }

}
