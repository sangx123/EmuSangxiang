package com.sangxiang.android.ui.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.sangxiang.android.AppConfig
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.utils.RegexMatcher
import com.sangxiang.android.utils.Utils
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_find_password.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class FindPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        mToolbar.setLeftOnClickListener(View.OnClickListener {
            this.onBackPressed()
        })
        SMSSDK.registerEventHandler(eventHandler)
        btn_get_yanzhengma.onClick {
            if(et_mobile.text.isNullOrBlank()){
                toast("手机号不能为空")
                return@onClick
            }

            if(!RegexMatcher.isPhoneNumber(et_mobile.text.toString())){
                toast("手机号输入不正确")
                return@onClick
            }
            SMSSDK.getVerificationCode("86",et_mobile.text.toString())
            btn_get_yanzhengma.isEnabled=false
            // 倒计时 60s
            mDisposables.add(Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        Log.e("sangxiang", "倒计时:"+(60-it))
                        btn_get_yanzhengma.text = "倒计时 " + (60 -it) + " 秒"
                    }
                    .doOnComplete {
                        Log.e("sangxiang", "倒计时完毕")
                        btn_get_yanzhengma.isEnabled=true
                        btn_get_yanzhengma.text="获取验证码"
                    }
                    .subscribe())

        }

        btn_submit.onClick {
            if(et_mobile.text.isNullOrBlank()){
                toast("手机号不能为空")
                return@onClick
            }

            if(!RegexMatcher.isPhoneNumber(et_mobile.text.toString())){
                toast("手机号输入不正确")
                return@onClick
            }
            if(et_password.text.length<6||et_password.text.length>20){
                toast("请输入6-20位密码")
                return@onClick
            }
            //验证验证码是否正确
            if(et_yanzhengma.text.toString()=="890828") {
                //通用验证码不用校验
                //直接注册
                apiResetPassword()
            }else{
                SMSSDK.submitVerificationCode("86", et_mobile.text.toString(), et_yanzhengma.text.toString())
            }

        }
    }

    //新增验证码功能
    private var eventHandler = object: EventHandler(){
        override fun afterEvent(event: Int, result: Int, data: Any) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            val msg = Message()
            msg.arg1 = event
            msg.arg2 = result
            msg.obj = data
            Handler(Looper.getMainLooper(), Handler.Callback { msg ->
                val event = msg!!.arg1
                val result = msg!!.arg2
                val data = msg!!.obj
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //处理成功得到验证码的结果 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        toast("验证码已发送")
                    } else {
                        //处理错误的结果
                        toast("验证码发送失败")
                        (data as Throwable).printStackTrace()
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //处理验证码验证通过的结果
                        Log.e("sangxiang","验证码验证通过！")
                        apiResetPassword()
                    } else {
                        //处理错误的结果
                        toast("验证码输入错误！")
                        (data as Throwable).printStackTrace()
                    }
                }
                //其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                false
            }).sendMessage(msg)
        }
    }

    private fun apiResetPassword() {
        var model= RegisterParam()
        model.mobile=et_mobile.text.toString()
        model.password= Utils.getMd5Hash(et_password.text.toString())
        EmucooApiRequest.getApiService().resetPassword(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<String>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<String>) {
                        if(t.respCode== AppConfig.SUCCESS) {
                            toast("密码重置成功！")
                        }else{
                            toast(t.respMsg)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

    }

    override fun onDestroy() {
        super.onDestroy()
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler()
    }
}
