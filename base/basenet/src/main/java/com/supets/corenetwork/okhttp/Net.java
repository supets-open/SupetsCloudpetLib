package com.supets.corenetwork.okhttp;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class Net {

    public static OkHttpClient createOkHttp3() {
        return Holder.getSingleton();
    }

    private static class Holder {

        private static OkHttpClient.Builder singleton;

        public static OkHttpClient getSingleton() {
            if (singleton == null) {
                synchronized (Holder.class) {
                    if (singleton == null) {
                        singleton = new OkHttpClient.Builder()
                                // .addNetworkInterceptor(new StethoInterceptor())
                                // .addInterceptor(new HttpLoggingInterceptor2())
                                //.addInterceptor(new MockInterceptor())
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS);
                        try {
                            TrustManager[] trustManagers = new TrustManager[]{new HTTPSTrustManager()};
                            SSLContext sslContext = SSLContext.getInstance("TLS");
                            sslContext.init(null, trustManagers, new SecureRandom());
                            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                            singleton.hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String hostname, SSLSession session) {
                                    return true;
                                }
                            }).sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return singleton.build();
        }

    }
}
