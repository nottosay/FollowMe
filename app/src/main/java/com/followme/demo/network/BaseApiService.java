package com.followme.demo.network;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wally.yan on 2016/11/23.
 */
public interface BaseApiService {

    @GET("{url}")
    Flowable<BaseResponse<Object>> get(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );


    @POST("{url}")
    Flowable<BaseResponse<Object>> post(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);

    @HEAD("{url}")
    Flowable<BaseResponse<Object>> head(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);

    @PUT("{url}")
    Flowable<BaseResponse<Object>> put(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);

    @Multipart
    @POST("{url}")
    Flowable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);


    @Streaming
    @GET
    Flowable<BaseResponse<Object>> downloadFile(@Url String fileUrl);

}
