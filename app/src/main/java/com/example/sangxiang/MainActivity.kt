package com.example.sangxiang

import android.app.Activity
import android.os.Bundle
import com.example.sangxiang.demo.EventBusActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : BaseActivity(),AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarDefaultColor()
        eventbusBtn.onClick {
            startActivity<EventBusActivity>()
        }
    }

}
