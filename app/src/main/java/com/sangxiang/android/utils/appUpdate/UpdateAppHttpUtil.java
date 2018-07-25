package com.sangxiang.android.utils.appUpdate;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sangxiang.android.network.EmucooApiRequest;
import com.sangxiang.android.utils.Utils;
import com.vector.update_app.HttpManager;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateAppHttpUtil implements HttpManager {
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
                callBack.onResponse("{\n" +
                        "  \"update\": \"Yes\",\n" +
                        "  \"new_version\": \"0.8.3\",\n" +
                        "  \"apk_file_url\": \"https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/apk/sample-debug.apk\",\n" +
                        "  \"update_log\": \"1，添加删除信用卡接口。\\r\\n2，添加vip认证。\\r\\n3，区分自定义消费，一个小时不限制。\\r\\n4，添加放弃任务接口，小时内不生成。\\r\\n5，消费任务手动生成。\",\n" +
                        "  \"target_size\": \"5M\",\n" +
                        "  \"new_md5\":\"b97bea014531123f94c3ba7b7afbaad2\",\n" +
                        "  \"constraint\": false\n" +
                        "}");
//            Map<String,String> map = new HashMap<String, String>();
//            map.put("appType","2");
//            map.put("appVersion","1.0.0");
//            EmucooApiRequest.getApiService().checkUpdate(map)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<String>() {
//
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(String response) {
//                            callBack.onResponse(response);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            callBack.onResponse("");
//                            //callBack.onError(e.getMessage().toString());
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });

    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull  String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        url="https://yf.emucoo.net/cfb/download/emucoo_test.apk";
        final String filePath=path;
        final JsDownloadListener listener = new JsDownloadListener() {
            @Override
            public void onStartDownload() {
                callback.onBefore();
            }

            @Override
            public void onProgress(int progress) {
                callback.onProgress(progress/100f, 1);
            }

            @Override
            public void onFinishDownload(File file) {
                callback.onResponse(file);
            }

            @Override
            public void onFail(String errorInfo) {
                callback.onError(errorInfo);
            }
        };


        DownloadUtils downloadUtils = new DownloadUtils("https://yf.emucoo.net/", listener);

        downloadUtils.download(url, filePath, new Observer(){

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                listener.onFinishDownload(new File(filePath));
            }

            @Override
            public void onError(Throwable t) {
                listener.onFail(t.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}