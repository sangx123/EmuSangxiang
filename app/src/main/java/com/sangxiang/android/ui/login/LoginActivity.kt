package com.sangxiang.android.ui.login

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.hawk.Hawk
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.ui.home.MainActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.Constants
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.network.model.UserModel
import com.sangxiang.android.network.param.LoginSubmit
import com.sangxiang.android.utils.SharePerferenceConfig
import com.sangxiang.android.utils.Utils
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(){
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        if(Hawk.get<String>(SharePerferenceConfig.user_phone).isNullOrBlank()) {
            et_mobile.setText("15821758991")
            et_password.setText("123456")
            val rxPermissions = RxPermissions(this)
            rxPermissions.setLogging(true)
            RxView.clicks(btn_login)
                    // Ask for permissions when button is clicked
                    .compose(rxPermissions.ensureEach(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    .subscribe(object:Observer<Permission>{
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(permission: Permission) {
                            error("Permission result $permission")
                            when {

                                permission.granted -> Login(et_mobile.text.toString(),et_password.text.toString())
                                permission.shouldShowRequestPermissionRationale ->
                                    // Denied permission without ask never again
                                    toast("Denied permission without ask never again")
                                else ->
                                    // Denied permission with ask never again
                                    // Need to go to the settings
                                    toast("Permission denied, can't enable the camera")
                            }
                        }

                        override fun onError(e: Throwable) {
                            toast("onError")
                        }

                    })

        }
        else{
            startActivity<MainActivity>()
            finish()
        }

    }

    private fun initView() {
        et_mobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE)
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE)
                }
            }
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE)
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE)
                }
                if (s.toString().isEmpty()) {
                    return
                }
                if (!s.toString().matches("[A-Za-z0-9]+".toRegex())) {
                    val temp = s.toString()
                    Toast.makeText(this@LoginActivity, "请输入数字或字母", Toast.LENGTH_SHORT).show()
                    s.delete(temp.length - 1, temp.length)
                    et_password.setSelection(s.length)
                }
            }
        })

        iv_show_pwd.onClick {
            if (et_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                iv_show_pwd.setImageResource(R.drawable.pass_visuable)
            } else {
                et_password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                iv_show_pwd.setImageResource(R.drawable.pass_gone)
            }
            val pwd = et_password.getText().toString()
            if (!TextUtils.isEmpty(pwd))
                et_password.setSelection(pwd.length)
        }

        clean_password.onClick {
            et_password.setText("")
        }

        iv_clean_phone.onClick {
            et_mobile.setText("")
        }
        regist.onClick {
            startActivity<RegisterActivity>()
        }
        forget_password.onClick {
            startActivity<FindPasswordActivity>()
        }
    }

    private fun Login(emailStr:String,passwordStr:String) {
        var model= LoginSubmit()
        model.mobile=emailStr
        model.password=Utils.getMd5Hash(passwordStr)
        EmucooApiRequest.getApiService().login(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<UserModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<UserModel>) {
                        Hawk.put<String>(SharePerferenceConfig.user_phone,emailStr)
                        Hawk.put<String>(SharePerferenceConfig.user_password,Utils.getMd5Hash(passwordStr))
                        Hawk.put<String>(SharePerferenceConfig.userToken,t.data!!.userToken)
                        Constants.setLoginUser(t.data)
                        startActivity<MainActivity>()
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

    }
}
