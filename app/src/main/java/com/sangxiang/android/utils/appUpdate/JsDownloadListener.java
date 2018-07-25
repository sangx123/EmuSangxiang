package com.sangxiang.android.utils.appUpdate;

import java.io.File;

/**
 * Description: 下载进度回调
 * Created by jia on 2017/11/30.
 * 人之所以能，是相信能
 */
public interface JsDownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload(File file);

    void onFail(String errorInfo);

}
