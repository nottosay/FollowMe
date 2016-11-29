package com.followme.demo.network;

import android.content.Context;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import javax.net.ssl.HostnameVerifier;


/**
 * Created by wally.yan on 2016/11/28.
 */

public class Windmill {

    public static RequestBuild url(String url) {
        return new RequestBuild(url);
    }

    /**
     * 开始cookie本地持久化
     */
    public static void cookie(Context context) {
        WeakReference<Context> mContext = new WeakReference<Context>(context);
        RetrofitClient.getInstance().cookie(mContext.get());
    }


    /**
     * 设置https证书
     *
     * @param certificates
     */
    public static void setCertificates(InputStream... certificates) {
        RetrofitClient.setCertificates(certificates);
    }

    /**
     * 设置https证书
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public static void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        RetrofitClient.setCertificates(certificates, bksFile, password);
    }


    /**
     * 设置hostname验证
     *
     * @param hostNameVerifier
     */
    public static void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        RetrofitClient.setHostNameVerifier(hostNameVerifier);
    }
}
