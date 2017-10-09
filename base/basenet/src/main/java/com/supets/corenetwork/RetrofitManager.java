package com.supets.corenetwork;


import com.supets.commons.utils.json.JSonUtil;
import com.supets.corenetwork.okhttp.Net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static class RetrofitLazyHolder {

        private static final boolean USE_HTTPS = false;   // 是否https访问协议
        private static final String BASE_URL = USE_HTTPS ? "https://cloudapi.10000pets.com" : "http://cloudapi.10000pets.com"; // 线上环境host

        private static final Retrofit RX_SINGLETON = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(JSonUtil.getGsonExcludeFields(null)))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(Net.createOkHttp3())
                .build();
    }

    public static Retrofit getRxInstance() {
        return RetrofitLazyHolder.RX_SINGLETON;
    }

}
