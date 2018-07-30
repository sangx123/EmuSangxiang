package com.sangxiang.android.demo

import android.app.Activity
import android.os.Bundle
import android.widget.CompoundButton
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import kotlinx.android.synthetic.main.activity_switch_button.*
import org.jetbrains.anko.toast
/*

The usage of SwitchButton is just like CheckBox. The basic control APIs of SwitchButton.

setChecked(boolean)
toggle()
Since SwitchButton has addition animation when checked status changed, there are two addition methods for disable animation for single operation.

setCheckedImmediately(boolean): like setChecked but NO animation.
toggleImmediately(): like toggle but NO animation.
From version 1.4.1 on, SwitchButton support operation without onCheckedChanged callback. It makes changing state in code more convenient. Using these methods to achieve that feature.

setCheckedNoEvent(boolean)
setCheckedImmediatelyNoEvent(boolean)
toggleNoEvent()
toggleImmediatelyNoEvent()

 */
class SwitchButtonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_button)
        //设置选中没有事件


        mSwitchButton.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    toast("mSwitchButton选中")
                }else{
                    toast("mSwitchButton没选中")
                }
            }
        })
        mSwitchButton1.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    toast("mSwitchButton1选中")
                }else{
                    toast("mSwitchButton1没选中")
                }
            }
        })
        mSwitchButton1.setCheckedNoEvent(false)
        //设置选中有事件
        mSwitchButton.isChecked = true
    }
}
