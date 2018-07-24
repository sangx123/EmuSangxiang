package com.sangxiang.android

import android.content.Context
import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger

/**
 * 02/02/2018  11:28 AM
 * Created by Zhang.
 */
open class EmucooBaseFragment : Fragment(){
    public lateinit var mDisposables: CompositeDisposable
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mDisposables = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

//    fun showProgress() {
//        getBaseActivity()?.mProgress?.show()
//    }
//
//    fun dismissProgress() {
//        getBaseActivity()?.mProgress?.dismiss()
//    }

    fun getBaseActivity(): BaseActivity? {

        val a = activity
        if (a is BaseActivity) {
            return a
        }
        return null


    }


}