package com.followme.demo.network;


import com.followme.demo.network.callback.Callback;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


/**
 * Created by wally.yan on 2016/8/2.
 */

public class SubscriberCallback<T> implements Subscriber<T> {

    private Callback<T> callback;

    public SubscriberCallback(Callback<T> callback) {
        this.callback = callback;
    }


    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onComplete() {
        if (callback != null) {
            callback.onFinish();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callback != null) {
            callback.onError();
            callback.onFinish();
        }
    }

    @Override
    public void onNext(T t) {
        if (callback != null) {
            callback.onSuccess(t);
        }
    }
}
