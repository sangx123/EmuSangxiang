package com.example.sangxiang

import android.app.Activity
import android.os.Bundle
import com.example.sangxiang.demo.EventBusActivity
import com.example.sangxiang.network.Constants
import com.example.sangxiang.utils.BitmapUtils
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
        testFun.onClick {
            var str="热门应用热门推荐热门活动热门应用热门推荐热11111"
            //str="热门"
            photoImage.setImageBitmap(BitmapUtils.getNameBitmap(str, dip(35f), dip(35f)))
        }
   }

}
