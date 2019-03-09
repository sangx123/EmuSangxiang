package com.xiang.one.network.error

import android.support.v4.app.FragmentActivity
import android.util.Log
import com.xiang.one.network.model.BaseResult
import io.reactivex.Observable
import org.json.JSONException
import java.net.ConnectException

object RxUtils {

    /**
     * Status code
     */
    private const val STATUS_OK = 200
    private const val STATUS_UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun <T : BaseResult<*>> handleGlobalError(activity: FragmentActivity): GlobalErrorTransformer<T> = GlobalErrorTransformer(

            // 通过onNext流中数据的状态进行操作
            onNextInterceptor = {
                when (it.respCode.toInt()) {
                    STATUS_UNAUTHORIZED -> {
                        Observable.error(TokenExpiredException())
                    }
                    else -> Observable.just(it)
                }
            },

            // 通过onError中Throwable状态进行操作
            onErrorResumeNext = { error ->
                when (error) {
                    is ConnectException -> {
                        Observable.error<T>(ConnectFailedAlertDialogException())
                    }
                    else -> Observable.error<T>(error)
                }
            },

            onErrorConsumer = { error ->
                when (error) {
                    is JSONException -> {
                        Log.w("RxUtils", "全局异常捕获-Json解析异常！")
                    }
                    is TokenExpiredException->{
                        Log.w("RxUtils", "全局异常捕获-TokenExpiredException！")
                    }
                    else -> {

                    }
                }
            }
    )
}
