package com.followme.demo.network.callback;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * Created by wally.yan on 2015/11/8.
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseResponse(ResponseBody body) throws IOException {
        return new String(body.bytes());
    }

}
