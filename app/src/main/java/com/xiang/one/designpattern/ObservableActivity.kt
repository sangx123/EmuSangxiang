package com.xiang.one.designpattern

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.xiang.one.BaseActivity
import com.xiang.one.R
import org.jetbrains.anko.AnkoLogger
/*
[观察者模式初探和java Observable模式](https://www.cnblogs.com/fingerboy/p/5468994.html)
 */
class ObservableActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observable)
        //测试
        val teacher = TeacherSubject()
        val zhangSan = StudentObserver("张三", teacher)
        val LiSi = StudentObserver("李四", teacher)
        val WangWu = StudentObserver("王五", teacher)

        teacher.setHomework("第二页第六题")
        teacher.setHomework("第三页第七题")
        teacher.setHomework("第五页第八题")
    }
}

//主题接口
interface Subject {
    //添加观察者
    fun addObserver(obj: Observer)

    //移除观察者
    fun deleteObserver(obj: Observer)

    //当主题方法改变时,这个方法被调用,通知所有的观察者
    fun notifyObserver()
}



class TeacherSubject : Subject {
    //用来存放和记录观察者
    private val observers = ArrayList<Observer>()
    //记录状态的字符串
    private var info: String? = null

    override fun addObserver(obj: Observer) {
        observers.add(obj)
    }

    override fun deleteObserver(obj: Observer) {
        val i = observers.indexOf(obj)
        if (i >= 0) {
            observers.remove(obj)
        }
    }

    override fun notifyObserver() {
        for (i in observers.indices) {
            observers[i].update(info!!)
        }
    }

    //布置作业的方法,在方法最后,需要调用notifyObserver()方法,通知所有观察者更新状态
    fun setHomework(info: String) {
        this.info = info
        Log.e("TeacherSubject","今天的作业是$info")
        this.notifyObserver()
    }

}

interface Observer {
    //当主题状态改变时,会将一个String类型字符传入该方法的参数,每个观察者都需要实现该方法
    fun update(info: String)
}

class StudentObserver//构造器用来注册观察者
(//学生的姓名,用来标识不同的学生对象
    private val name: String, t: TeacherSubject) : Observer{

    //保存一个Subject的引用,以后如果可以想取消订阅,有了这个引用会比较方便

    init {
        //每新建一个学生对象,默认添加到观察者的行列
        t.addObserver(this)
    }


    override fun update(info: String) {
        Log.e("StudentObserver:",name + "得到作业:" + info)
    }

}