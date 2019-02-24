package com.sangxiang.android.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class FindPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)
        mToolbar.setLeftOnClickListener(View.OnClickListener {
            this.onBackPressed()
        })
    }
}
