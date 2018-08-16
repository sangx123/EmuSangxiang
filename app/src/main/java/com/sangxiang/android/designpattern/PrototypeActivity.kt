package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
/*
原型设计模式
[原型模式的作用和意义](https://blog.csdn.net/huangyimo/article/details/80390001)
[原型模式使用](https://blog.csdn.net/zhengzhb/article/details/7393528)
*/
class PrototypeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prototype)
        //原型模式使用
        val cp = ConcretePrototype()
        for (i in 0..9) {
            val clonecp = cp.clone() as ConcretePrototype
            clonecp.show()
        }


    }


}
 open class Prototype : Cloneable {
    public override fun clone(): Prototype{
        var prototype: Prototype? = null
        try {
            prototype = super.clone() as Prototype
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return prototype!!
    }
}
class ConcretePrototype : Prototype() {
    fun show() {
        println("原型模式实现类")
    }
}
