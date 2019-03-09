package com.xiang.one.utils.dialog

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.xiang.one.R
import kotlinx.android.synthetic.main.dialog_alert_common.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class AlertDialogs : Dialog {
    constructor(context: Context) : this(context,R.style.Dialog_Fullscreen)

    constructor(context: Context, themeResId: Int) : super(context, themeResId){
        init()
        this.setCanceledOnTouchOutside(false)
    }

    private fun init() {
        setContentView(R.layout.dialog_alert_common)
        initBottom()
        mBtnLeft.onClick { dismiss() }
        mBtnRight.onClick { dismiss() }
    }
    private fun initBottom() {
        val dialogWindow = this.window
        val lp = dialogWindow!!.attributes
        // 设置显示动画
        window.setWindowAnimations(R.style.PickerAnimStyle)
        dialogWindow.setGravity(Gravity.CENTER)
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        //lp.alpha = 0.7f; // 透明度
        dialogWindow.attributes = lp
    }

    fun setTitle(str:String):AlertDialogs{
        mTitle.text=str
        return this
    }

    fun setContent(str:String):AlertDialogs{
        mContent.text=str
        return this
    }

    fun setBtnLeftTxt(str:String):AlertDialogs{
        mBtnLeft.text=str
        return this
    }

    fun setBtnRightTxt(str:String):AlertDialogs{
        mBtnRight.text=str
        return this
    }

    fun setLeftClickListener(clickListener:(alertDialog:AlertDialogs,button:View)->Unit):AlertDialogs{
        mBtnLeft.onClick {
            clickListener.invoke(this@AlertDialogs,mBtnLeft)
        }
        return this
    }
    fun setRightClickListener(clickListener:(alertDialog:AlertDialogs,button:View)->Unit ):AlertDialogs{
        mBtnRight.onClick {
            clickListener.invoke(this@AlertDialogs,mBtnRight)
        }
        return this
    }
}
