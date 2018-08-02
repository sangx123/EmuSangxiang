package com.sangxiang.android.widgets

import android.app.Activity
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.startActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clickable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.CommentNum
import com.sangxiang.android.network.param.ParamCommentSelectIn
import com.sangxiang.android.utils.button_textview.setSolidTheme
import org.jetbrains.anko.toast


class  WidgetCommentView : RelativeLayout, LifecycleObserver {
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context,attrs,defStyleAttr){
        initView(context)
    }
    var model: ParamCommentSelectIn?=null
    lateinit var mCommentTxt:TextView
    lateinit var mContent:Context
    private fun initView(context: Context) {
        // 加载布局
        var view= LayoutInflater.from(context).inflate(R.layout.widget_comment_layout, this,true);
        mCommentTxt=view.findViewById(R.id.mCommentTxt)
        mContent=context
        //getData(1)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun ON_RESUME() {
       if(model!=null){
           EmucooApiRequest.getApiService().getCommentNum(model!!)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(object : Observer<CommentNum> {
                       override fun onComplete() {

                       }
                       override fun onSubscribe(d: Disposable) {
                           (context as BaseActivity).mDisposables.add(d)
                       }

                       override fun onNext(t: CommentNum) {
                           if(t.num==0){
                               mCommentTxt.text="暂无评论"
                           }else{
                               mCommentTxt.text=t.num.toString()+"条评论"
                           }

                       }

                       override fun onError(e: Throwable) {
                           e.printStackTrace()
                       }

                   })
       }
    }

    fun setCommentNum(param: ParamCommentSelectIn) {
        model=param
        (mContent as BaseActivity).lifecycle.addObserver(this)
        mCommentTxt.visibility=visibility
        mCommentTxt.onClick {
            mContent.toast("mCommentTxt")
        }
    }
}