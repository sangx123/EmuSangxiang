package com.sangxiang.android.ui.login

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.orhanobut.hawk.Hawk
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.Constants
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.network.model.UserModel
import com.sangxiang.android.network.param.LoginSubmit
import com.sangxiang.android.ui.home.MainActivity
import com.sangxiang.android.utils.SharePerferenceConfig
import com.sangxiang.android.utils.Utils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import cn.smssdk.SMSSDK.RESULT_COMPLETE
import cn.smssdk.SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE
import cn.smssdk.SMSSDK.EVENT_GET_VERIFICATION_CODE
import android.os.Looper
import android.os.Message
import android.util.Log
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.mob.MobCommunicator
import com.mob.tools.utils.UIHandler.sendMessage
import com.sangxiang.android.AppConfig
import com.sangxiang.android.utils.RegexMatcher
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.internal.util.NotificationLite.accept
import java.util.concurrent.TimeUnit


class RegisterActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        SMSSDK.registerEventHandler(eventHandler)
        mToolbar.setLeftOnClickListener(View.OnClickListener {
            this.onBackPressed()
        })

        btn_get_yanzhengma.onClick {
            if(et_name.text.isNullOrBlank()){
                toast("用户名不能为空")
                return@onClick
            }
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

        btn_register.onClick {
            if(et_name.text.isNullOrBlank()){
                toast("用户名不能为空")
                return@onClick
            }
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
                apiRegister()
            }else{
                SMSSDK.submitVerificationCode("86", et_mobile.text.toString(), et_yanzhengma.text.toString())
            }

        }
    }

    fun apiRegister(){
        var model= RegisterParam()
        model.name=et_name.text.toString()
        model.mobile=et_mobile.text.toString()
        model.password=Utils.getMd5Hash(et_password.text.toString())
        EmucooApiRequest.getApiService().register(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<UserModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<UserModel>) {
                        if(t.respCode== AppConfig.SUCCESS) {
                            Hawk.put<String>(SharePerferenceConfig.user_phone, model.mobile)
                            Hawk.put<String>(SharePerferenceConfig.user_password, model.password)
                            Hawk.put<String>(SharePerferenceConfig.userToken, t.data!!.userToken)
                            Constants.setLoginUser(t.data)
                            startActivity<MainActivity>()
                            finish()
                        }else{
                            toast(t.respMsg)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    //新增验证码功能
    private var eventHandler = object:EventHandler(){
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
                        apiRegister()
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

    override fun onDestroy() {
        super.onDestroy()
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler()
    }


}
