package com.sangxiang.android.demo

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.eventbus.PhotoMessageEvent
import com.sangxiang.android.network.Constants
import com.sangxiang.android.utils.Utils
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
