package com.sangxiang.android.utils.appUpdate;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Description:
 * Created by jia on 2017/11/30.
 * 人之所以能，是相信能
 */
public interface DownloadService {
    /*
    // option 1: a resource relative to your base URL
    @GET("/resource/example.zip")
    Call<ResponseBody> downloadFileWithFixedUrl();
    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
    */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
