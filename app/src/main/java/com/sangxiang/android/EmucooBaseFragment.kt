package com.sangxiang.android

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

/**
 * 02/02/2018  11:28 AM
 * Created by Zhang.
 */
abstract class EmucooBaseFragment(layoutId:Int) : Fragment(){

    constructor(layoutId:Int,needCache:Boolean) : this(layoutId) {
        this.needCache=needCache
        this.layoutId=layoutId
    }

    //添加支持
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

    //添加fragment缓存支持
    var rootView: View? = null
    var isCacheFragmentView=false
    var needCache=false
    var layoutId:Int=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Fragment之间切换时每次都会调用onCreateView方法，导致每次Fragment的布局都重绘，无法保持Fragment原有状态。
        if(needCache) {
            if (rootView == null) {
                rootView = inflater.inflate(layoutId, container, false)
                isCacheFragmentView=false
            }else{
                isCacheFragmentView=true
            }

            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            val parent = rootView!!.parent as ViewGroup?
            parent?.removeView(rootView)
        }else{
            rootView = inflater.inflate(layoutId, container, false)
        }


        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(needCache){
            if(!isCacheFragmentView)
                initData()
        }else{
            initData()
        }
    }




    fun getBaseActivity(): BaseActivity? {

        val a = activity
        if (a is BaseActivity) {
            return a
        }
        return null
    }
    abstract fun initData()

}