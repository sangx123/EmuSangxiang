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



class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        SMSSDK.registerEventHandler(eventHandler)
        mToolbar.setLeftOnClickListener(View.OnClickListener {
            this.onBackPressed()
        })

        btn_get_yanzhengma.onClick {
            SMSSDK.getVerificationCode("86",et_mobile.text.toString())
        }

        btn_register.onClick {
            if(et_mobile.text.isNullOrEmpty()){
                toast("手机号不能为空")
                return@onClick
            }
            if(et_password.text.isNullOrEmpty()){
                toast("密码不能少于6位")
                return@onClick
            }
            if(et_password.text.toString() != et_password1.text.toString()){
                toast("两次输入的密码不一致")
                return@onClick
            }
            //验证验证码是否正确
            SMSSDK.submitVerificationCode("86", et_mobile.text.toString(),et_yanzhengma.text.toString())

        }
    }

    fun apiRegister(){
        var model= RegisterParam()
        model.mobile=et_mobile.text.toString()
        model.password=Utils.getMd5Hash(et_password.text.toString())
        EmucooApiRequest.getApiService().register(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<String>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<String>) {
//                            Hawk.put<String>(SharePerferenceConfig.user_phone,emailStr)
//                            Hawk.put<String>(SharePerferenceConfig.user_password,Utils.getMd5Hash(passwordStr))
//                            Constants.setLoginUser(t)
//                            startActivity<MainActivity>()
//                            finish()
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
