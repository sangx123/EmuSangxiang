package com.sangxiang.android.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.sangxiang.android.R
import com.sangxiang.android.network.error.RxUtils
import com.sangxiang.android.network.model.BaseResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONException
import java.net.ConnectException

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btnSimpleError.setOnClickListener {
            fetchError(obsToastError)
        }
        btnConnectError.setOnClickListener {
            fetchError(obsConnectError)
        }
        btnTokenError.setOnClickListener {
            fetchError(obsTokenError)
        }
    }

    /**
     * 简单的异常全局处理，比如弹一个toast，JSONException
     */
    private val obsToastError: Observable<BaseResult<UserInfo>> =
            Observable.error(JSONException("JSONException"))

    /**
     * 复杂的异步处理，比如弹出一个dialog，用户操作决定流的下一步走向
     */
    private val obsConnectError: Observable<BaseResult<UserInfo>> =
            Observable.error(ConnectException())

    /**
     * 十分复杂的处理，比如token失效，用户跳转login界面，重新登录成功后，继续重新请求
     */
    private val obsTokenError =
            Observable.create<BaseResult<UserInfo>> { emitter ->
                val entity = BaseResult<UserInfo>(
                        respCode = "401",
                        respMsg = "unauthorized",
                        data = null
                )
                emitter.onNext(entity)
            }
    data class UserInfo(var aa:Int)

    private fun fetchError(observable: Observable<BaseResult<UserInfo>>) =
            observable
                    .compose(RxUtils.handleGlobalError<BaseResult<UserInfo>>(this))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object:Observer<BaseResult<UserInfo>>{
                        override fun onComplete() {
                            Log.e("sangxiang","onComplete")
                        }

                        override fun onSubscribe(d: Disposable) {
                            Log.e("sangxiang","onSubscribe")
                        }

                        override fun onNext(t: BaseResult<UserInfo>) {
                            Log.e("sangxiang","onNext")
                        }

                        override fun onError(e: Throwable) {
                            Log.e("sangxiang","onError")
                        }

                    })

}