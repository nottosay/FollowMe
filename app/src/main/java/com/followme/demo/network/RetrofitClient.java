package com.followme.demo.network;

import android.content.Context;

import org.reactivestreams.Publisher;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.followme.demo.network.https.HttpsUtils.getSslSocketFactory;

/**
 * Created by wally.yan on 2016/11/23.
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 30;

    private static volatile RetrofitClient mInstance;

    private static HostnameVerifier mHostNameVerifier;
    private static SSLSocketFactory mSslSocketFactory;


    private OkHttpClient okHttpClient;
    private Retrofit retrofit;


    private RetrofitClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        if (mSslSocketFactory != null) {
            okHttpClientBuilder.sslSocketFactory(mSslSocketFactory);
        } else {
            okHttpClientBuilder.sslSocketFactory(getSslSocketFactory());
        }
        if (mHostNameVerifier != null) {
            okHttpClientBuilder.hostnameVerifier(mHostNameVerifier);
        } else {
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        okHttpClientBuilder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(5, 15, TimeUnit.SECONDS));
        okHttpClient = okHttpClientBuilder.build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitClient.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitClient();
                }
            }
        }
        return mInstance;
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     *
     */
    void cookie(Context context) {
        okHttpClient.newBuilder().cookieJar(new CookieManger(context)).build();
    }

    /**
     * @param headers
     */
    void header(Map<String, String> headers) {
        okHttpClient.newBuilder().addInterceptor(new HeaderInterceptor(headers)).build();
    }

    FlowableTransformer schedulersTransformer() {
        return new FlowableTransformer() {

            @Override
            public Publisher apply(Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    <T> FlowableTransformer<BaseResponse<T>, T> transformer() {

        return new FlowableTransformer() {

            @Override
            public Publisher apply(Flowable upstream) {
                return upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }

    private class HandleFuc<T> implements Function<BaseResponse<T>, T> {

        @Override
        public T apply(BaseResponse<T> tBaseResponse) throws Exception {
            if (!tBaseResponse.isSuccess()) {
                throw new RuntimeException(tBaseResponse.code + "" + tBaseResponse.msg != null ? tBaseResponse.msg : "");
            }
            return tBaseResponse.data;
        }
    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Flowable<T>> {


        @Override
        public Flowable<T> apply(Throwable throwable) throws Exception {
            return Flowable.error(ExceptionHandle.handleException(throwable));
        }
    }

    /**
     * 设置https证书
     *
     * @param certificates
     */
    static void setCertificates(InputStream... certificates) {
        if (mInstance != null) {
            throw new RuntimeException("Certificate settings should be initialized before");
        }
        mSslSocketFactory = getSslSocketFactory(certificates, null, null);
    }

    /**
     * 设置https证书
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    static void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        if (mInstance != null) {
            throw new RuntimeException("Certificate settings should be initialized before");
        }
        mSslSocketFactory = getSslSocketFactory(certificates, bksFile, password);
    }


    /**
     * 设置hostname验证
     *
     * @param hostNameVerifier
     */
    static void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        if (mInstance != null) {
            throw new RuntimeException("Certificate settings should be initialized before");
        }
        mHostNameVerifier = hostNameVerifier;
    }
}
