package com.sangxiang.android.demo

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.utils.dialog.AlertDialogs
import kotlinx.android.synthetic.main.activity_dialog.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class DialogActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        nextBtn.onClick {
            AlertDialogs(this@DialogActivity).setRightClickListener { alertDialog, button ->
                alertDialog.dismiss()
            }.show()
        }
    }
}
