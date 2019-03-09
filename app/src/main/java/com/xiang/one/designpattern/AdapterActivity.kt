package com.xiang.one.designpattern

import android.app.Activity
import android.os.Bundle
import com.xiang.one.BaseActivity
import com.xiang.one.R

/**
 * [适配器模式](https://www.jianshu.com/p/9d0575311214)
 */
class AdapterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)
        //类的适配器模式调用
        var mAdapter =  Adapter()
        mAdapter.Request()
        //对象的适配器模式调用
        var mAdapter1 =  Adapter1(Adaptee())
        mAdapter1.Request()
    }

    interface Target {

        //这是源类Adapteee没有的方法
        fun Request()
    }

    open class Adaptee {

        fun SpecificRequest() {}
    }
    //-----------------------------类的适配器模式，原本由于接口不兼容而不能一起工作的那些类可以在一起工作----------------------------
    //适配器Adapter继承自Adaptee，同时又实现了目标(Target)接口。
    class Adapter : Adaptee(),Target {

        //目标接口要求调用Request()这个方法名，但源类Adaptee没有方法Request()
        //因此适配器补充上这个方法名
        //但实际上Request()只是调用源类Adaptee的SpecificRequest()方法的内容
        //所以适配器只是将SpecificRequest()方法作了一层封装，封装成Target可以调用的Request()而已
        override fun Request() {
            this.SpecificRequest();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------

    //——-----------------------对象的适配器模式---------------------------------------------------------------------------------
    class Adapter1(var adaptee:Adaptee):Target{

        override fun Request() {
            // 这里是使用委托的方式完成特殊功能
            adaptee.SpecificRequest();
        }
    }


}
