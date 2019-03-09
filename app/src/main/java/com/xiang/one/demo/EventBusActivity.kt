package com.xiang.one.demo

import android.app.Activity
import android.os.Bundle
import com.xiang.one.BaseActivity
import com.xiang.one.R
import com.xiang.one.eventbus.PhotoMessageEvent
import com.xiang.one.network.Constants
import com.xiang.one.utils.Utils
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_event_bus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity

class EventBusActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_bus)
        EventBus.getDefault().register(this)
        mDisposables.add(RxView.clicks(btn).subscribe{
            EventBus.getDefault().post( PhotoMessageEvent())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun Photo( messageEvent: PhotoMessageEvent) {
       // Utils.loadNormalPicToIv(Constants.getUser().headImgUrl,photoImage)
    }
}
