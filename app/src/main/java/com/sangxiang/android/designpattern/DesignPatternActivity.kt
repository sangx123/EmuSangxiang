package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import kotlinx.android.synthetic.main.activity_design_pattern.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class DesignPatternActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_pattern)
        btn1.onClick {
            startActivity<FactoryActivity>()
        }
        btn2.onClick {
            startActivity<SingletonActivity>()
        }
        btn3.onClick { startActivity<BuilderActivity>() }
    }
}
