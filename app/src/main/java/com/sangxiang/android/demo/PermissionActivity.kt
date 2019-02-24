package com.sangxiang.android.demo

import android.Manifest
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.jakewharton.rxbinding2.view.RxView
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_permission.*


class PermissionActivity : BaseActivity() ,AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        val rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(true)
        RxView.clicks(btn)
                // Ask for permissions when button is clicked
                .compose(rxPermissions.ensureEach(Manifest.permission.CAMERA))
                .subscribe(object:Observer<Permission>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        error("Permission result $permission")
                        if (permission.granted) {
                            toast("权限允许")
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            toast("Denied permission without ask never again")
                        } else {
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
//     var disposable: Disposable?=null
//    override fun onDestroy() {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose()
//        }
//        super.onDestroy()
//    }
}
