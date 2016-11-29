package com.followme.demo.network;


/**
 * 网络返回基类
 *
 * @param <T>
 */
public class BaseResponse<T> {

    public String code;
    public String msg;
    public T data;

    public boolean isSuccess() {
        return "0000".equals(code);
    }

}
