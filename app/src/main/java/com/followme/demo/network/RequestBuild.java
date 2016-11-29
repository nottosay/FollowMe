package com.followme.demo.network;

import org.reactivestreams.Subscriber;

import java.util.Map;

import io.reactivex.Flowable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by wally.yan on 2016/11/29.
 */

public class RequestBuild {
    private BaseApiService apiService;
    private String url;
    private Map<String, String> params;

    public RequestBuild(String url) {
        this.apiService = RetrofitClient.getInstance().create(BaseApiService.class);
        this.url = url;
    }

    public RequestBuild params(Map<String, String> params) {
        if (url == null) {
            throw new RuntimeException("Url must is not null!");
        }
        this.params = params;
        return this;
    }


    public RequestBuild header(Map<String, String> header) {
        if (url == null) {
            throw new RuntimeException("Url must is not null!");
        }
        RetrofitClient.getInstance().header(header);
        return this;
    }

    public void get(Subscriber<? super T> subscriber) {
        Flowable flowable = apiService.get(url, params);
        flowable.subscribe(subscriber);
    }

    public void post(Subscriber<? super T> subscriber) {
        Flowable flowable = apiService.post(url, params);
        flowable.subscribe(subscriber);
    }

    public void head(Subscriber<? super T> subscriber) {
        Flowable flowable = apiService.head(url, params);
        flowable.subscribe(subscriber);
    }

    public void put(Subscriber<? super T> subscriber) {
        Flowable flowable = apiService.put(url, params);
        flowable.subscribe(subscriber);
    }


}
