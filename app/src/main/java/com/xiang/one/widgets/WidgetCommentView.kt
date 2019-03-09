package com.xiang.one.widgets

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
import com.xiang.one.BaseActivity
import com.xiang.one.R
import com.xiang.one.network.Api
import com.xiang.one.network.model.BaseResult
import com.xiang.one.network.model.CommentNum
import com.xiang.one.network.param.ParamCommentSelectIn
import com.xiang.one.utils.button_textview.setSolidTheme
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
           Api.getApiService().getCommentNum(model!!)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(object : Observer<BaseResult<CommentNum>> {
                       override fun onComplete() {

                       }
                       override fun onSubscribe(d: Disposable) {
                           (context as BaseActivity).mDisposables.add(d)
                       }

                       override fun onNext(t: BaseResult<CommentNum>) {
                           if(t.data!!.num==0){
                               mCommentTxt.text="暂无评论"
                           }else{
                               mCommentTxt.text=t.data!!.num.toString()+"条评论"
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