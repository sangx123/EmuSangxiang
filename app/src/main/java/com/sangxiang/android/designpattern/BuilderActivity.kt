package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import kotlinx.android.synthetic.main.activity_builder.*
import java.lang.StringBuilder

//构造者模式
//[建造者模式（Builder Pattern）- 最易懂的设计模式解析](https://blog.csdn.net/carson_ho/article/details/54910597)
class BuilderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_builder)
        //调用过程
        //逛了很久终于发现一家合适的电脑店
        //找到该店的老板和装机人员
        var director =  Director()
        var builder =  ConcreteBuilder()
        //沟通需求后，老板叫装机人员去装电脑
        director.Construct(builder);
        //装完后，组装人员搬来组装好的电脑
        var computer = builder.GetComputer()
        //组装人员展示电脑给小成看
        mTxt.text=computer.Show()

    }
}

//定义组装的过程（Builder）：组装电脑的过程
abstract class Builder {

    //第一步：装CPU
    //声明为抽象方法，具体由子类实现
    public abstract fun BuildCPU()

    //第二步：装主板
    //声明为抽象方法，具体由子类实现
    public abstract fun BuildMainboard()

    //第三步：装硬盘
    //声明为抽象方法，具体由子类实现
    public abstract fun BuildHD()

    //返回产品的方法：获得组装好的电脑
    public abstract fun GetComputer(): Computer
}

//电脑城老板委派任务给装机人员（Director）
class Director {
    //指挥装机人员组装电脑
    fun Construct(builder: Builder) {
        builder.BuildCPU()
        builder.BuildMainboard()
        builder.BuildHD()
    }
}

//创建具体的建造者（ConcreteBuilder）:装机人员
class ConcreteBuilder : Builder() {
    //创建产品实例
    var computer = Computer();

    //组装产品
    override fun BuildCPU() {
        computer.Add("组装CPU")
    }

    override fun BuildMainboard() {
        computer.Add("组装主板")
    }

    override fun BuildHD() {
        computer.Add("组装主板")
    }

    //返回组装成功的电脑
    override fun GetComputer(): Computer {
        return computer
    }
}

//定义具体产品类（Product）：电脑
public class Computer {

    //电脑组件的集合
    private var parts = ArrayList<String>()

    //用于将组件组装到电脑里
    fun Add(part: String) {
        parts.add(part)
    }

    fun Show():String {
        var sb=StringBuilder()

        for (item in parts) {
            sb.appendln("组件:${item}装好了")
        }
        sb.appendln("电脑组装完成，请验收")
        return sb.toString()
    }
}

