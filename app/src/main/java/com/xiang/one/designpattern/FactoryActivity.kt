package com.xiang.one.designpattern

import android.app.Activity
import android.os.Bundle
import com.xiang.one.BaseActivity
import com.xiang.one.R
import kotlinx.android.synthetic.main.activity_factory.*


//工厂设计模式
/*
[为什么要用工厂模式](https://blog.csdn.net/legendj/article/details/9006767)

[工厂模式 五种写法总结](https://blog.csdn.net/zxt0601/article/details/52798423)

[工厂模式](https://juejin.im/entry/58f5e080b123db2fa2b3c4c6)
每天看一篇总结，背下来
 */
class FactoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factory)
        //简单工厂模式
        var baseDao=Factory.createMySql()
        mTxt.text=Factory.createMySql().getModel()
        //工厂方法模式
        mTxt.text=MySqlFactory().create().getModel()
        //抽象工厂模式
        //一个工厂产生2份或者多份具体类
        var obj1= AndroidFactory().createInterfaceController()
        var obj2=AndroidFactory().createOperationController()
    }
    //-------------------------------------简单工厂模式-------------------------------
    interface BaseDao{
        fun getModel():String
        fun getList()
    }

    class MySql:BaseDao {
        override fun getList() {

        }

        override fun getModel():String{
            return "MySqlModel"
        }
    }

    class SqlServer:BaseDao{
        override fun getModel():String {
            return "SqlServerModel"
        }

        override fun getList() {

        }

    }

    class Factory{
        companion object {
            //多方法工厂（常用）
            fun createMySql():MySql{
                return MySql()
            }

            fun createSqlServer():SqlServer{
                return SqlServer()
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------工厂方法模式------------------------------------------
    interface IFactofy{
        fun create():BaseDao
    }
    class MySqlFactory:IFactofy{
        override fun create():BaseDao {
            return MySql()
        }
    }
    class SqlServerFactory:IFactofy{
        override fun create(): BaseDao {
            return SqlServer()
        }

    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------抽象工厂模式------------------------------------------
    interface OperationController {
        fun control()
    }

    interface UIController {
        fun display()
    }

    inner class AndroidOperationController : OperationController {
        override fun control() {
            println("AndroidOperationController")
        }
    }

    inner class AndroidUIController : UIController {
        override fun display() {
            println("AndroidInterfaceController")
        }
    }

    inner class IosOperationController : OperationController {
        override fun control() {
            println("IosOperationController")
        }
    }

    inner class IosUIController : UIController {
        override fun display() {
            println("IosInterfaceController")
        }
    }

    interface SystemFactory {
        fun createOperationController(): OperationController
        fun createInterfaceController(): UIController
    }

    inner class AndroidFactory : SystemFactory {
        override fun createOperationController(): OperationController {
            return AndroidOperationController()
        }

        override fun createInterfaceController(): UIController {
            return AndroidUIController()
        }
    }

    inner class IosFactory : SystemFactory {
        override fun createOperationController(): OperationController {
            return IosOperationController()
        }

        override fun createInterfaceController(): UIController {
            return IosUIController()
        }
    }

}
