package com.sangxiang.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sangxiang.android.utils.status_bar_utils.Eyes
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * 02/02/2018  11:26 AM
 * Created by Zhang.
 */

abstract class BaseActivity : AppCompatActivity() {

//    var mOnResultListener: ((mNotificationRequestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null


    private val mOnResultListeners= mutableListOf<(Int,Int,Intent?)->Unit>()
    public lateinit var mDisposables:CompositeDisposable



    public fun getDisposables() = mDisposables

    fun onResult(listener: (requestCode:Int,resultCode:Int, Intent?) -> Unit): Unit {
//        mOnResultListener = listener
        mOnResultListeners.add(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivities.add(this)
        mDisposables = CompositeDisposable()

        /*
        mProgress.setOnDismissListener(object : DialogInterface.OnDismissListener{
            override fun onDismiss(p0: DialogInterface?) {
                mDisposables.dispose()
            }

        })
        */
    }

    /**
     *
     * @param clz
     * @param bundle 参数可不传
     */
    fun gotoActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        mOnResultListener?.invoke(mNotificationRequestCode, resultCode, data)
        mOnResultListeners.forEach {
            it.invoke(requestCode,resultCode,data)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        mActivities.remove(this)
        mDisposables.dispose()
        super.onDestroy()
    }

    companion object {
        protected var mActivities: MutableList<BaseActivity> = ArrayList()
        val TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED"
        private val TAG_FAKE_STATUS_BAR_VIEW = 12345
    }

    fun Activity.setStatusBarDefaultColor() {
        Eyes.setStatusBarLightMode(this, getResources().getColor(R.color.white))
    }
}
