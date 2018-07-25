package com.sangxiang.android.utils.appUpdate;


import android.util.Log;

import com.vector.update_app.HttpManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Description: 带进度 下载请求体
 * Created by jia on 2017/11/30.
 * 人之所以能，是相信能
 */
public class JsResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private HttpManager.FileCallback callback;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public JsResponseBody(ResponseBody responseBody, HttpManager.FileCallback callback) {
        this.responseBody = responseBody;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: "+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != callback) {
                    if (bytesRead != -1) {
                        callback.onProgress(((int) (totalBytesRead * 100 / responseBody.contentLength()))/100f,1);
                    }

                }
                return bytesRead;
            }
        };

    }
}
