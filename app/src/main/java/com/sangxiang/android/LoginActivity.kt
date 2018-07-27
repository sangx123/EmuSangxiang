package com.sangxiang.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import com.sangxiang.android.network.Constants
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.UserModel
import com.sangxiang.android.network.param.LoginSubmit
import com.sangxiang.android.utils.Utils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

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
        setStatusBarDefaultColor()
        if(Constants.getUser()==null) {
            //edit1.setText("emu")
            //edit2.setText("123456")
            btn.setOnClickListener { Login(edit1.text.toString(),edit2.text.toString()) }
        }
        else{
            startActivity<MainActivity>()
            finish()
        }
    }

    private fun Login(emailStr:String,passwordStr:String) {
        var model= LoginSubmit()
        model.mobile=emailStr
        model.password=Utils.getMd5Hash(passwordStr)
        EmucooApiRequest.getApiService().login(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserModel> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: UserModel) {

                        Constants.setLoginUser(t)
                        startActivity<MainActivity>()
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

    }
}
