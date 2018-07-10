package com.example.sangxiang.demo

import android.app.Activity
import android.os.Bundle
import com.example.sangxiang.BaseActivity
import com.example.sangxiang.R
import com.example.sangxiang.eventbus.PhotoMessageEvent
import com.example.sangxiang.network.Constants
import com.example.sangxiang.utils.Utils
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
        setStatusBarDefaultColor()
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
        Utils.loadNormalPicToIv(Constants.getUser().headImgUrl,photoImage)
    }
}
