package com.followme.demo.network.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.ResponseBody;


/**
 * Created by wally.yan on 2015/11/8.
 */
public abstract class BitmapCallback extends Callback<Bitmap> {
    @Override
    public Bitmap parseResponse(ResponseBody body) throws Exception {
        return BitmapFactory.decodeStream(body.byteStream());
    }

}
